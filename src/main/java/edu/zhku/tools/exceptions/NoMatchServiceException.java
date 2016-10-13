package edu.zhku.tools.exceptions;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2016/1/15 16:40
 * @since 0.1
 */
public class NoMatchServiceException extends ServiceException {

    public NoMatchServiceException() {
        super("没有合适的服务！");
    }

    public NoMatchServiceException(String message) {
        super(message);
    }

    public NoMatchServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMatchServiceException(Throwable cause) {
        super(cause);
    }

    protected NoMatchServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
