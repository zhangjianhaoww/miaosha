package tech.bilian.web.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.bilian.web.model.SuccessKilled;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的位置
@ContextConfiguration("classpath:spring/spring-config.xml")
public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        long id = 1000l;
        long phone = 15505198532l;
        int i = successKilledDao.insertSuccessKilled(id, phone);
        System.out.println("i===================" + i);
    }

    @Test
    public void queryByIdWithSeckill() {
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000l, 15505198531l);
        System.out.println(successKilled);
    }
}