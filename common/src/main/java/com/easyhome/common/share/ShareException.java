package com.easyhome.common.share;

/**
 * 分享异常
 *
 * @author zhoulu
 * @date 13-12-12
 */
public class ShareException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** HTTP请求出错时，服务器返回的错误状态码 */
    private int mStatusCode = 0;

    /**
     * 构造函数。
     *
     * @param message    HTTP请求出错时，服务器返回的字符串
     * @param statusCode HTTP请求出错时，服务器返回的错误状态码
     */
    public ShareException(String message, int statusCode) {
        super(message);
        mStatusCode = statusCode;
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified detail message.
     *
     * @param detailMessage the detail message for this exception.
     */
    public ShareException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace, the
     * specified detail message and the specified cause.
     *
     * @param detailMessage the detail message for this exception.
     * @param throwable
     */
    public ShareException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * HTTP请求出错时，服务器返回的错误状态码。
     *
     * @return 服务器返回的错误状态码
     */
    public int getStatusCode() {
        return mStatusCode;
    }
}
