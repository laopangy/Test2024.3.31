package org.javaboy.vhr.utils;

import java.lang.reflect.Method;

/**
 * @className    : Utility
 * @description    : 工具类
 * @summary    :
 * @history    : ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 */
public class Utility {
    /**
     * @param object     : 方法所在的类对象
     * @param methodName : 方法名
     * @methodName        : getMethodByName
     * @description     : 根据方法名称获取方法对象
     * @return        : Method类型对象
     * @history        : ------------------------------------------------------------------------------
     * date			version		modifier		remarks
     * ------------------------------------------------------------------------------
     * 2021/01/01	1.0.0		sheng.zheng		初版
     */
    public static Method getMethodByName(Object object, String methodName) {
        Class<?> class1 = object.getClass();
        Method retItem = null;
        Method[] methods = class1.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method item = methods[i];
            if (item.getName().equals(methodName)) {
                retItem = item;
                break;
            }
        }
        return retItem;
    }
}

