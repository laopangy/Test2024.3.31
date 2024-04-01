package org.javaboy.vhr.base;

import org.javaboy.vhr.exception.ExceptionCodes;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className    : BaseController
 * @description    : 控制器基类
 * @summary    : 支持事务处理，并提供操作成功的方法
 * @history    : ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 */
@RestController
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class BaseController {
    /**
     * @param <T> : 模板类型
     * @methodName        : successResponse
     * @description     : 操作成功，返回信息不含数据体
     * @return        : 操作成功的返回码和消息，不含数据体
     * @history        : ------------------------------------------------------------------------------
     * date			version		modifier		remarks
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     */
    protected <T> BaseResponse<T> successResponse() {
        BaseResponse<T> response = new BaseResponse<T>();
        response.setCode(ExceptionCodes.SUCCESS.getCode());
        response.setMessage(ExceptionCodes.SUCCESS.getMessage());
        return response;
    }

    /**
     * @param <T>  : 模板类型
     * @param data : 模板类型的数据
     * @methodName        : successResponse
     * @description     : 操作成功，返回信息含数据体
     * @return            : 操作成功的返回码和消息，并包含数据体
     * @history         :
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     */
    protected <T> BaseResponse<T> successResponse(T data) {
        BaseResponse<T> response = new BaseResponse<T>();
        response.setCode(ExceptionCodes.SUCCESS.getCode());
        response.setMessage(ExceptionCodes.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }
}
