package tech.bilian.web.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.bilian.web.dto.Exposer;
import tech.bilian.web.dto.SeckillExecution;
import tech.bilian.web.exception.RepeatKillException;
import tech.bilian.web.exception.SeckillCloseException;
import tech.bilian.web.model.Seckill;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-config.xml", "classpath:spring/spring_service.xml"})
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
    }

    @Test
    public void getBySeckillId() {
        Seckill seckill = seckillService.getBySeckillId(1001L);
        logger.info("seckill={}", seckill.toString());
    }

    @Test
    public void exportSeckillUrl() {
        Exposer exposer = seckillService.exportSeckillUrl(1001L);
        System.out.println("zhljkdjldjfsldfsdfsfsfsdfdfsfsa");
        logger.info("exposer={}", exposer.getMd5());
    }

    @Test
    public void executeSeckill() {
        try{
            String md5 = "1506c3c6912ca0ddc0f731aea8e493b2";
            long seckillId = 1001L;
            long userPhone = 12345678902L;
            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            logger.info("result={}", execution.getStateInfo());
        }catch (RepeatKillException e1){
            logger.info(e1.getMessage());
        }
        catch (SeckillCloseException e2){
            logger.info(e2.getMessage());
        }
    }
}