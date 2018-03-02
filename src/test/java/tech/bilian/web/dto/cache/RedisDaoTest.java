package tech.bilian.web.dto.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.bilian.web.dao.SeckillDao;
import tech.bilian.web.model.Seckill;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-config.xml", "classpath:spring/spring_service.xml"})

public class RedisDaoTest {
    private long id = 1002;

    @Resource
    private RedisDao redisDao;

    @Resource
    private SeckillDao seckillDao;

    @Test
    public void getSeckill() {
        Seckill seckill = redisDao.getSeckill(id);
        if(seckill == null){
            seckill = seckillDao.queryById(id);
            if(seckill != null){
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
                seckill = redisDao.getSeckill(id);
                System.out.println(seckill);
            }
        }
    }

    @Test
    public void putSeckill() {
    }
}