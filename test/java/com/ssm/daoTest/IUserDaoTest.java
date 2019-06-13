package com.ssm.daoTest;


import com.ssm.dao.IUserDao;
import com.ssm.dao.UserMapper;
import com.ssm.model.User;
import com.ssm.model.UserExample;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class IUserDaoTest {
    @Autowired
    private IUserDao dao;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectUser() throws Exception {
        long id = 1;
        User user = dao.selectUser(id);
        System.out.println(user.getUsername());
    }
    @Test
    public void testDate(){
        Date date = new Date();
        System.out.println(date);
    }
    @Test
    public void testDateFormat(){
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andEmailEqualTo("lijiang@bbktel.com");
        List<User> users = userMapper.selectByExample(example);
        Date date = users.get(0).getRegtime();
        System.out.println(date);

    }
    @Test
    public void testUserList(){
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        List<User> users = userMapper.selectByExample(example);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("users", JSONArray.fromObject(users));
        System.out.println(jsonObject.toString());
        System.out.println(jsonObject.get("users"));
    }


    // 测试httpclient
    @Test
    public void doGetTestOne(){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build(); // 获得http客户端
        HttpGet httpGet = new HttpGet("http://www.baidu.com/"); // 创建get请求
        CloseableHttpResponse response = null;  // 响应模型
        try{
            response = httpClient.execute(httpGet); // 由客户端发送get请求
            HttpEntity responseEntity = response.getEntity(); // 从响应模型中获取响应实体
            System.out.println("Response Status："+response.getStatusLine());
            if(responseEntity!=null){
                System.out.println("Response Length:" + responseEntity.getContentLength());
                System.out.println("Response Content:" + EntityUtils.toString(responseEntity));
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(httpClient!=null){
                    httpClient.close();
                }
                if(response!=null){
                    response.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
