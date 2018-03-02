package tech.bilian.web.service;

import tech.bilian.web.dto.Exposer;
import tech.bilian.web.dto.SeckillExecution;
import tech.bilian.web.exception.RepeatKillException;
import tech.bilian.web.exception.SeckillCloseException;
import tech.bilian.web.exception.SeckillExcepiton;
import tech.bilian.web.model.Seckill;

import java.util.List;

public interface SeckillService {

    /**
     *获取所有的秒杀记录
     *
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     *获取单个秒杀记录
     *
     * @param seckillId
     * @return
     */
    Seckill getBySeckillId(long seckillId);

    /**
     * 秒杀开始时输出秒杀接口地址，否则输出系统当前时间和秒杀时间
     *
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws  SeckillExcepiton, SeckillCloseException, RepeatKillException;
}
