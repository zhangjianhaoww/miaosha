package tech.bilian.web.exception;

/**
 * 所有秒杀相关异常
 *
 */
public class SeckillExcepiton extends RuntimeException{
    public SeckillExcepiton(String message) {
        super(message);
    }

    public SeckillExcepiton(String message, Throwable cause) {
        super(message, cause);
    }
}
