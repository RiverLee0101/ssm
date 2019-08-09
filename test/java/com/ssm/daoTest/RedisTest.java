package com.ssm.daoTest;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RedisTest {
    @Test
    public void redisTester(){
        Jedis jedis = new Jedis("localhost",6379,10000);
        int i=0;
        try{
            long start = System.currentTimeMillis();
            while(true){
                long end = System.currentTimeMillis();
                if(end-start>=1000){
                    break;
                }
                i++;
                jedis.set("test"+i, i+"");
            }
        }finally{
            jedis.close();
        }
        System.out.println("Redis每秒操作："+i+"次");
    }
}
