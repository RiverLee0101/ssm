package com.ssm.daoTest;

import net.sf.json.JSONObject;
import org.apache.lucene.index.Terms;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MatchQuery;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.naming.directory.SearchResult;
import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class ElasticsearchSearchAPITest {

    private TransportClient client=null;
    public static final String INDEX = "fendo";
    public static final String TYPE = "fendodate";
    @SuppressWarnings("unchecked")
    @Before
    public void getClient() throws Exception{
        //  1.设置连接的集群名称
        Settings settings = Settings.builder().put("cluster.name","my-application").build();
        //  2.连接集群
        client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));
        //  3.打印集群名称
        System.out.println(client.toString());
    }

    /*
    * 通过query查询文档内容
    */
    @Test
    public void testSearchApi(){
        SearchResponse response = client.prepareSearch(INDEX).setTypes(TYPE).setQuery(matchQuery("user","lijiang")).get();
        SearchHit[] hits = response.getHits().getHits();
        for(int i=0; i<hits.length;i++){
            String json = hits[i].getSourceAsString();
            JSONObject object = JSONObject.fromObject(json);
            Set<String> strings = object.keySet();
            for(String str : strings){
                System.out.println(object.get(str));
            }
        }
    }
    /*
    * 滚动查询
    */
    @Test
    public void testScrollApi() throws ExecutionException, InterruptedException {
        MatchQueryBuilder qb = matchQuery("message","test");
        SearchResponse response = client.prepareSearch(INDEX).addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(100).get();
        do{
            for(SearchHit hit : response.getHits().getHits()){
                String json = hit.getSourceAsString();
                JSONObject object = JSONObject.fromObject(json);
                Set<String> strings = object.keySet();
                for(String str :strings){
                    System.out.println(object.get(str));
                }
            }
            response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute().get();
        }while(response.getHits().getHits().length != 0);
    }
    /*
    * MultiSearch API
    */
    @Test
    public void testMultiSearchApi(){
        SearchRequestBuilder srb1 = client.prepareSearch().setQuery(QueryBuilders.queryStringQuery("test")).setSize(1);
        SearchRequestBuilder srb2 = client.prepareSearch().setQuery(QueryBuilders.matchQuery("user","lijiang")).setSize(1);
        MultiSearchResponse multiSearchResponse = client.prepareMultiSearch().add(srb1).add(srb2).get();
        long nbHits = 0;
        for(MultiSearchResponse.Item item : multiSearchResponse.getResponses()){
            SearchResponse response = item.getResponse();
            nbHits += response.getHits().getTotalHits();

        }
        System.out.println(nbHits);
    }
    /*
    * 聚合
    */
    @Test
    public void testAggregations(){
        SearchResponse searchResponse = client.prepareSearch()
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(AggregationBuilders.terms("lijiang").field("user"))
                .addAggregation(AggregationBuilders.dateHistogram("2019-06-15").field("postData").dateHistogramInterval(DateHistogramInterval.YEAR)).get();
        Terms lijiang = searchResponse.getAggregations().get("user");
        Histogram postDate= searchResponse.getAggregations().get("2019-06-15");
    }
    /*
    * 判断文档是否达到设置的最大数量
    */
    @Test
    public void testTerminate(){
        SearchResponse searchResponse = client.prepareSearch(INDEX).setTerminateAfter(3).get(); // 如果达到这个数量，提前终止
        if(searchResponse.isTerminatedEarly()){
            System.out.println(searchResponse.getHits().totalHits);
        }
    }
}
