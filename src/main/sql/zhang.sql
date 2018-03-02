-- 创建数据库
create database seckill;

-- 使用数据库
use seckill;

CREATE TABLE seckill (
    `seckill_id` INT NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
    `name` VARCHAR(120) NOT NULL COMMENT '商品名称',
    `number` INT NOT NULL COMMENT '库存数量',
    `start_time` TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
    `end_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀结束时间',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`seckill_id`),
    KEY idx_start_time (`start_time`),
    KEY idx_end_time (`end_time`),
    KEY idx_create_time (`create_time`)
)  ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=UTF8 COMMENT '秒杀库存表';


insert into
seckill(name, number, start_time, end_time)
values
('1000元秒杀iPhone10', 100, '2017-12-30 00:00:00', '2018-1-1 00:00:00'),
('500元秒杀iPad2', 200, '2017-12-30 00:00:00', '2018-1-1 00:00:00'),
('300元秒杀小米4', 300, '2017-12-30 00:00:00', '2018-1-1 00:00:00'),
('100秒杀寝室长', 5, '2017-12-30 00:00:00', '2018-1-1 00:00:00');


create table success_killed(
 `seckill_id` INT NOT NULL  COMMENT '商品库存id',
  `user_phone` bigint not null comment'用户手机号',
  `state` tinyint not null default -1 comment'状态提示：-1：无效 0：成功 1：已付款',
  `create_time` timestamp not null comment'创建时间',
  primary key(seckill_id, user_phone),
  key idx_create_time(create_time)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=UTF8 COMMENT '秒杀成功明细表';

