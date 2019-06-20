package com.ssm.daoTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.script.Script;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class ElasticsearchIndexAPITest {

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
    * 1.手动方式
    */
    @Test
    public void JsonDocument() throws UnknownHostException {
        String json = "{"+
                "\"user\":\"deepredapple\"," +
                "\"postDate\":\"2018-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        IndexResponse indexResponse = client.prepareIndex("fendo","fendodate").setSource(json).get();
        System.out.println(indexResponse.getResult());
    }
    /*
    * 2.Map 方式
    */
    @Test
    public void MapDocument(){
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("user","huantiansheng");
        json.put("postDate", "2019-06-15");
        json.put("message", "today is sunday");
        IndexResponse indexResponse = client.prepareIndex("blog","blog").setSource(json).get();
        System.out.println(indexResponse.getResult());
    }
    /*
    * 3.序列化方式
    */
    @Test
    public void JACKSONDocument() throws JsonProcessingException {
        Blog blog = new Blog();
        blog.setUser("John");
        blog.setPostDate("2018-06-29");
        blog.setMessage("try out ElasticSearch");
        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes = mapper.writeValueAsBytes(blog);
        IndexResponse response = client.prepareIndex("fendo","fendodate").setSource(bytes).get();
        System.out.println(response.getResult());
    }
    public class Blog implements Serializable {
        String user;
        String postDate;
        String message;
        public void setUser(String user){
            this.user = user;
        }
        public void setPostDate(String postDate){
            this.postDate = postDate;
        }
        public void setMessage(String message){
            this.message = message;
        }
        public String getUser(){return user;}
        public String getPostDate(){return postDate;}
        public String getMessage(){return message;}
    }
    /*
    * 4.XContentBuilder帮助类方式
    */
    @Test
    public void XContentBuilderDocument() throws IOException{
        XContentBuilder builder = jsonBuilder().startObject()
                .field("user", "xcontentdocument")
                .field("postDate", "2018-06-30")
                .field("message", "this is ElasticSearch").endObject();
        IndexResponse response = client.prepareIndex(INDEX, TYPE).setSource(builder).get();
        System.out.println(response.getResult());
    }

    /*
    * Get API 通过id去取文档内容
    */
    @Test
    public void testGetApi(){
        // setOperationThreaded设置为true是在不同的线程里执行此操作
        GetResponse getResponse = client.prepareGet(INDEX, TYPE, "AWtWCcuelpooUlTSGlyc").setOperationThreaded(false).get();
        Map<String, Object> map = getResponse.getSource();
        if(map==null){
            System.out.println("No this document!");
        }else {
            Set<String> keySet = map.keySet();
            for(String key : keySet){
                Object o = map.get(key);
                System.out.println(o.toString());
            }
        }

    }
    /*
    * Delete API 根据id删除文档
    */
    @Test
    public void testDeleteApi(){
        GetResponse getResponse = client.prepareGet(INDEX, TYPE, "AWtWCcuelpooUlTSGlyc").setOperationThreaded(false).get();
        System.out.println(getResponse.getSource());
        DeleteResponse deleteResponse = client.prepareDelete(INDEX, TYPE, "AWtWCcuelpooUlTSGlyc").get();
        System.out.println(deleteResponse.getResult());
    }
    /*
    * 通过查询条件删除
    */
    @Test
    public void deleteByQuery(){
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("user", "jack"))
                .source(INDEX).get();
        long deleted = response.getDeleted(); // 删除文档数量
        System.out.println(deleted);
    }
    /*
    * 异步回调,回调的方式执行删除，适合大数据量的删除操作
    */
    @Test
    public void deleteByQueryAsync(){
        for(int i=1300;i<3000;i++){
            DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                    .filter(QueryBuilders.matchQuery("user", "hhh " + i))
                    .source(INDEX)
                    .execute(new ActionListener<BulkByScrollResponse>() {
                        @Override
                        public void onResponse(BulkByScrollResponse response) {
                            long deleted = response.getDeleted();
                            System.out.println("删除的文档数量为="+deleted);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            System.out.println("Failure");
                        }
                    });
        }
    }
    /*
    * 更新文档
    * 使用UpdateRequest进行更新
    */
    @Test
    public void testUpdateApi() throws IOException, ExecutionException, InterruptedException{
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(INDEX);
        updateRequest.type(TYPE);
        updateRequest.id("AWtVzvtT8K4paE07nMyu");
        updateRequest.doc(jsonBuilder().startObject().field("user","Jack Huang").endObject());
        client.update(updateRequest).get();
    }
    /*
    * 更新文档
    * 使用prepareUpdate()
    */
    @Test
    public void testUpdatePrepareUpdate() throws IOException {
        //client.prepareUpdate(INDEX, TYPE, "AWtVzvtT8K4paE07nMyu").setScript(new Script("ctx._source.user = \"DeepRedApple\"")).get();
        client.prepareUpdate(INDEX, TYPE, "AWtVzvtT8K4paE07nMyu").setDoc(jsonBuilder().startObject().field("user", "DeepRedApple update").endObject()).get();
    }
    /*
    * 更新文档
    * 存在更新，否则插入
    */
    @Test
    public void testUpsert() throws IOException,ExecutionException,InterruptedException{
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, "AWtVzvtT8K4paE07nMyu")
                .source(jsonBuilder()
                        .startObject()
                        .field("user", "hhh")
                        .field("postDate", "2018-02-14")
                        .field("message", "ElasticSearch")
                        .endObject());
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, "AWtVzvtT8K4paE07nMyu")
                .doc(jsonBuilder()
                        .startObject()
                        .field("user", "LZH")
                        .endObject())
                .upsert(indexRequest); //如果不存在，就增加indexRequest
        client.update(updateRequest).get();
    }
    /*
    * 通过文档id一次获取多个文档
    */
    @Test
    public void testMultiGetApi(){
        MultiGetResponse responses = client.prepareMultiGet()
                .add(INDEX, TYPE, "AWtVzvtT8K4paE07nMyu")
                .add(INDEX, TYPE, "AWtZ8o3gb2ILFvpwEaj6","AWtVJr9zTE5BzRZT3zd9")
                .add("blog","blog","AWtZ9T9qb2ILFvpwEaj7")
                .get();
        for(MultiGetItemResponse itemResponse : responses){
            GetResponse response = itemResponse.getResponse();
            if(response.isExists()){
                String source = response.getSourceAsString();
                JSONObject jsonObject = JSONObject.fromObject(source);
                Set<String> sets = jsonObject.keySet();
                for(String str : sets){
                    System.out.println("key -> " + str);
                    System.out.println("value -> "+jsonObject.get(str));
                    System.out.println("===============");
                }

            }
        }
    }
    /*
    * 项目中MultiGetRequestBuilder
    */
    @Test
    public void testMultiGetRequestBuilder(){
        MultiGetRequestBuilder mgrb = client.prepareMultiGet();
        mgrb.add(INDEX, TYPE,"1");
        mgrb.add(INDEX, TYPE, "2");
        MultiGetResponse response = null;
        response = mgrb.execute().actionGet();
        for(MultiGetItemResponse multiGetItemResponse : response.getResponses()){
            String source = multiGetItemResponse.getResponse().getSourceAsString();
            System.out.println(source);
        }


    }


    /*
    * Bulk 实现批量插入
    */
    @Test
    public void testBulkApi() throws IOException{
        BulkRequestBuilder requestBuilder = client.prepareBulk();
        requestBuilder.add(client.prepareIndex(INDEX, TYPE, "1")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "张三")
                        .field("postDate", "2018-05-01")
                        .field("message", "zhangSan message test")
                        .endObject()
                )
        );
        requestBuilder.add(client.prepareIndex(INDEX, TYPE, "2")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "李四")
                        .field("postDate", "2016-09-10")
                        .field("message", "Lisi message test")
                        .endObject()
                )
        );
        BulkResponse bulkResponse = requestBuilder.get();
        if(bulkResponse.hasFailures()){
            System.out.println("error!");
        }
    }
}
