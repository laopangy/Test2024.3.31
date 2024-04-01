package org.javaboy.vhr.exception;

/**
 * @className    : ExceptionCodes
 * @description    : 异常信息枚举类
 * @summary    :
 * @history    : ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 */
public enum ExceptionCodes {
    // 0-99，reserved for common exception
    SUCCESS(0, "message.SUCCESS", "操作成功"),
    FAILED(1, "message.FAILED", "操作失败"),
    ERROR(99, "message.ERROR", "操作异常"),
    ARGUMENTS_ERROR(2, "message.ARGUMENTS_ERROR", "参数错误"),
    TASKID_NOT_EXIST(16, "message.TASKID_NOT_EXIST", "任务ID不存在，可能已过期销毁"),
    TASKID_NOT_RIGHTS(17, "message.TASKID_NOT_RIGHTS", "无权访问此任务ID"),
    SESSION_IS_NULL(18, "message.SESSION_IS_NULL", "session为空，请重新登录"),

    ARGUMENTS_IS_EMPTY(22, "message.ARGUMENTS_IS_EMPTY", "参数值不能为空"),
    ADD_OBJECT_FAILED(30, "message.ADD_OBJECT_FAILED", "新增对象失败"),

    ;    // 定义结束

    // 返回码
    private int code;

    public int getCode() {
        return this.code;
    }

    // 返回消息ID
    private String messageId;

    public String getMessageId() {
        return this.messageId;
    }

    // 返回消息
    private String message;

    public String getMessage() {
        return this.message;
    }

    ExceptionCodes(int code, String messageId, String message) {
        this.code = code;
        this.messageId = messageId;
        this.message = message;
    }
}
