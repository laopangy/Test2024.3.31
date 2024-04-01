package org.javaboy.vhr.base;

import lombok.Data;

/**
 * @className    : BaseResponse
 * @description    : 基本响应消息体对象
 * @summary    :
 * @history    : ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 */
@Data
public class BaseResponse<T> {
    // 响应码
    private int code;

    // 响应消息
    private String message;

    // 响应实体信息
    private T data;

    // 简单起见，屏蔽下列信息
    // 分页信息
    // private Page page;

    // 附加通知信息
    // private Additional additional;
}

