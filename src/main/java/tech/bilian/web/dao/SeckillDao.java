package tech.bilian.web.dao;

import org.apache.ibatis.annotations.Param;
import tech.bilian.web.model.Seckill;

import java.util.Date;
import java.util.List;

public interface SeckillDao {

    /**
     * 减少库存
     *
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀对象
     *
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     *
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll (@Param("offset") int offset, @Param("limit") int limit);
}
