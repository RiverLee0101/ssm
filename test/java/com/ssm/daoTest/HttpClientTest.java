package com.ssm.daoTest;


import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class HttpClientTest {
    // 普通的无参数get请求：打开一个url,抓取url响应结果
    @Test
    public void doGet() throws Exception{
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建Get请求
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        CloseableHttpResponse response = null;
        try{
            // 执行get请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if(response.getStatusLine().getStatusCode()==200){
                // 请求体内容
                String content = EntityUtils.toString(response.getEntity(),"UTF-8");
                // 打出请求体内容
                System.out.println(content);
                // 内容长度
                System.out.println("内容长度："+content.length());
            }
        }finally {
            if(response!=null){
                response.close();
            }
            httpClient.close();
        }
    }

    // 执行带参数的get请求：模拟使用百度搜索关键字“java”，并获取响应结果
    @Test
    public void doGetParam() throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 定义请求的参数
        URI uri = new URIBuilder("http://www.baidu.com/s").setParameter("wd","java").build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode()==200){
                String content = EntityUtils.toString(response.getEntity(),"UTF-8");
                System.out.println(content);
                System.out.println("内容长度："+content.length());
            }
        }finally {
            if(response!=null){
                response.close();
            }
            httpClient.close();
        }

    }

    //普通的无参数POST请求：设置header来伪装浏览器请求
    @Test
    public void doPost() throws Exception{
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建Http Post请求
        HttpPost httpPost = new HttpPost("http://www.baidu.com");
        // 伪装浏览器请求
        //httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        CloseableHttpResponse response = null;
        try{
            // 执行http post请求
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode()==200){
                String content = EntityUtils.toString(response.getEntity(),"UTF-8");
                System.out.println(content);
                System.out.println("内容长度："+content.length());
            }else{
                System.out.println("请求失败");
            }
        }finally {
            if(response!=null){
                response.close();
            }
            httpClient.close();
        }
    }

    // 执行带参数的POST请求：模拟开源中国检索java，并伪装浏览器请求
    @Test
    public void doPostParam() throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://www.oschina.net/search");
        // 设置两个post参数，一个是scope,一个是q
        List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
        parameters.add(new BasicNameValuePair("scope","project"));
        parameters.add(new BasicNameValuePair("q","java"));
        // 构造一个form表单式的实体
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
        // 将请求实体设置到HttpPost对象中
        httpPost.setEntity(formEntity);
        // 伪装浏览器
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode()==200){
                String content = EntityUtils.toString(response.getEntity(),"UTF-8");
                System.out.println(content);
                System.out.println("内容长度："+content.length());
            }
        }finally {
            if(response!=null){
                response.close();
            }
            httpClient.close();
        }
    }
}
