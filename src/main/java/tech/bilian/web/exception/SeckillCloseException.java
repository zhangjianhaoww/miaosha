package tech.bilian.web.exception;

/**
 * 秒杀关闭时异常
 *
 */
public class SeckillCloseException extends SeckillExcepiton{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
