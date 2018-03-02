package tech.bilian.web.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.bilian.web.model.Seckill;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的位置
@ContextConfiguration("classpath:spring/spring-config.xml")
public class SeckillDaoTest {
    @Resource
    private SeckillDao seckillDao;


    @Test
    public void reduceNumber() {
        Date killTime =new Date();
        int i = seckillDao.reduceNumber(1004l, killTime);
        System.out.println(i);
    }

    @Test
    public void queryById() {
        long id = 1001l;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill);

    }

    @Test
    public void queryAll() {
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
    }
}