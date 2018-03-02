package tech.bilian.web.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import tech.bilian.web.dao.SeckillDao;
import tech.bilian.web.dao.SuccessKilledDao;
import tech.bilian.web.dto.Exposer;
import tech.bilian.web.dto.SeckillExecution;
import tech.bilian.web.dto.cache.RedisDao;
import tech.bilian.web.enums.SeckillStateEnum;
import tech.bilian.web.exception.RepeatKillException;
import tech.bilian.web.exception.SeckillCloseException;
import tech.bilian.web.exception.SeckillExcepiton;
import tech.bilian.web.model.Seckill;
import tech.bilian.web.model.SuccessKilled;
import tech.bilian.web.service.SeckillService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillDao seckillDao;

    @Resource
    private SuccessKilledDao successKilledDao;

    @Resource
    private RedisDao redisDao;

    private final String slat = "slfsf#*$$*@$@sdsjdDJDDKDJG&×……×%×￥（%……&×…………￥￥￥……（";


    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 5);
    }


    public Seckill getBySeckillId(long seckillId) {
        return seckillDao.queryById(seckillId);
    }


    public Exposer exportSeckillUrl(long seckillId) {

        //优化点：缓存优化 一致性维护建立在超时的基础上
        //秒杀对象一般是不会改变的
        /**
         * get from cache
         * if null
         *      get db
         *      put cache
         *
         *
         */
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null){
            seckill = seckillDao.queryById(seckillId);

            if(seckill == null)
            {
                return new Exposer(false, seckillId);
            }else{
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        System.out.println("startTime:" + startTime.toString());
        System.out.println("endTime:" + endTime.toString());
        System.out.println("nowTime:" + nowTime.toString());
        if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime())
        {
            System.out.println("zhangjianhao");
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        //转化特定字符串的过程
        String md5 = getMd5(seckillId);
        System.out.println("md5");
        return new Exposer(true, md5, seckillId);
    }



    /**
     * 使用注解控制事务的优点
     * 1：开发团队达成一致约定，明确标注事务方法的编程风格
     * 2：保证事务方法的执行时间尽可能短，不要穿插其他的网络操作，剥离到方法外部
     * 3：不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务操作
     *
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillExcepiton, SeckillCloseException, RepeatKillException {
        if(md5 == null || !getMd5(seckillId).equals(md5))
        {
            throw new SeckillExcepiton("seckill data rewrite");
        }

        //执行秒杀逻辑：减库存+记录购买行为
        Date nowTime = new Date();
        try{
            //先执行insert再执行update来减少行级锁的控制时间
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            //唯一的seckillId和userPhone
            if(insertCount <= 0)
            {
                throw new RepeatKillException("seckill repeated");
            }

            else
            {
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if(updateCount <= 0)
                {
                    throw new SeckillCloseException("seckill is closed");
                }
                else
                {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }

            }

        }catch(SeckillCloseException e1){
            throw e1;

        }catch(RepeatKillException e2){
            throw e2;

        } catch (Exception e){
            logger.error(e.getMessage(), e);

            //把所有编译期异常转化为运行时异常
            throw new SeckillExcepiton("seckill inner error: " + e.getMessage());
        }



    }


    private String getMd5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

}
