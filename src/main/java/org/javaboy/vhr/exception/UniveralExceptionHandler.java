package org.javaboy.vhr.exception;

import org.javaboy.vhr.base.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @className    : UniveralExceptionHandler
 * @description    : 通用异常处理类
 * @summary    :
 * @history    : ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 */
@ControllerAdvice
public class UniveralExceptionHandler {
    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param e : Exception类型的异常
     * @methodName    : handleException
     * @description    : 拦截非业务异常
     * @return    : JSON格式的异常信息
     * @history :
     * ------------------------------------------------------------------------------
     * date			version		modifier		remarks
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleException(Exception e) {
        //将异常信息写入日志
        logger.error(e.getMessage(), e);
        //输出通用错误代码和信息
        Map<String, Object> map = new HashMap<>();
        map.put("code", ExceptionCodes.ERROR.getCode());
        map.put("message", ExceptionCodes.ERROR.getMessage());
        return map;
    }

    /**
     * @param e : BaseException类型的异常
     * @methodName    : handleBaseException
     * @description    : 拦截业务异常
     * @return        : JSON格式的异常信息
     * @history        : ------------------------------------------------------------------------------
     * date			version		modifier		remarks
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     */
    @ResponseBody
    @ExceptionHandler(BaseException.class)
    public Map<String, Object> handleBaseException(BaseException e) {
        //将异常信息写入日志
        logger.error("业务异常：code：{}，messageId：{}，message：{}", e.getCode(), e.getMessageId(), e.getMessage());
        //输出错误代码和信息
        Map<String, Object> map = new HashMap<>();
        map.put("code", e.getCode());
        map.put("message", e.getMessage());
        return map;
    }

}
