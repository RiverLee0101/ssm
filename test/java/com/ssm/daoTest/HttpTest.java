package com.ssm.daoTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.TRANSACTION_MODE;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class HttpTest {
    
    /*
    * java发送http的get和post方式的请求
    */
    
    @Test // 向指定URL发送GET方式的请求
    public void doGet(){
        String url = "http://www.baidu.com/s";
        String param = "wd=java";
        String result="";
        String urlName = url + "?" + param;
        try{
            URL realUrl = new URL(urlName);
            // 打开和URL之间的链接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept","*/*");
            conn.setRequestProperty("connection","Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的链接
            conn.connect();
            // 获取所有的响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍历所有的响应头字段
            for(String key : map.keySet()){
                System.out.println(key+"-->"+map.get(key));
            }
            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line = in.readLine()) != null){
                result+=line;
            }
        }catch (Exception e){
            System.out.println("发出get请求出现异常："+e);
            e.printStackTrace();
        }
        System.out.println("响应结果为："+result);
    }
    
    
    @Test // 向指定URL发送POST方式的请求
    public void doPost(){
        String url = "http://localhost:8080/httpRequest/doPost";
        String param = "questionId=1&content=美丽人生";
        String result="";
        try{
            URL realUrl = new URL(url);
            // 打开和URL之间的链接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取 URLConnection 对象对应的输出流
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush 输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line=in.readLine()) != null){
                result += "\n"+line;
            }
        }catch (Exception e){
            System.out.println("发送POST请求出现异常："+e);
            e.printStackTrace();
        }
        System.out.println("响应结果为："+result);
        
    }
    
}
