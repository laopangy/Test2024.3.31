package org.javaboy.vhr.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @className    : LogUtil
 * @description    : 日志工具类
 * @summary    :
 * @history    : ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 */
@Slf4j
public class LogUtil {
    /**
     * @param e : Exception对象
     * @methodName    : error
     * @description    : 输出异常信息
     * @history    : ------------------------------------------------------------------------------
     * date			version		modifier		remarks
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     */
    public static void error(Exception e) {
        e.printStackTrace();
        String ex = getString(e);
        log.error(ex);
    }

    /**
     * @param ex : Exception对象
     * @methodName    : getString
     * @description    : 获取Exception的getStackTrace信息
     * @return    : 错误调用栈信息
     * @history    : ------------------------------------------------------------------------------
     * date			version		modifier		remarks
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     */
    public static String getString(Exception ex) {

        StringBuilder stack = new StringBuilder();
        StackTraceElement[] sts = ex.getStackTrace();
        for (StackTraceElement st : sts) {
            stack.append(st.toString()).append("\r\n");
        }
        return stack.toString();
    }
}

