package org.javaboy.vhr.base;

import lombok.Data;
import org.javaboy.vhr.exception.ExceptionCodes;

/**
 * @className    : BaseException
 * @description    : 异常信息基类
 * @summary    : 可以处理系统异常和自定义异常
 * @history    : ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 */
@Data
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 4359709211352401087L;

    // 异常码
    private int code;

    // 异常信息ID
    private String messageId;

    // 异常信息
    private String message;

    // =============== 以下为各种构造函数，重载 ===================================

    public BaseException(String message) {
        this.message = message;
    }

    public BaseException(String message, Throwable e) {
        this.message = message;
    }

    public BaseException(int code, String message) {
        this.message = message;
        this.code = code;
    }

    public BaseException(ExceptionCodes e) {
        this.code = e.getCode();
        this.messageId = e.getMessageId();
        this.message = e.getMessage();
    }

    public BaseException(ExceptionCodes e, String message) {
        this.code = e.getCode();
        this.messageId = e.getMessageId();
        this.message = e.getMessage() + ":" + message;
    }

    public BaseException(int code, String message, Throwable e) {
        this.message = message;
        this.code = code;

    }
}

