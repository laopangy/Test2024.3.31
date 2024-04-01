package org.javaboy.vhr.constants;

/**
 * @className    : TaskConstants
 * @description    : 任务处理相关常量
 * @summary    :
 * @history    : ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2022/08/18	1.0.0		sheng.zheng		初版
 */
public class TaskConstants {
    // 任务缓存过期时间，单位毫秒，即任务处理完成后，设置此时长，超期销毁
    public static final int PROC_EXPIRE_TIME = 60000;

    // 线程池核心线程数
    public static final int CORE_POOL_SIZE = 5;

    // 线程池最大线程数
    public static final int MAX_POOL_SIZE = 100;

    // 线程池KeepAlive参数，单位秒
    public static final long KEEP_ALIVE_SECONDS = 10;

    // 任务队列最大数目
    public static final int MAX_TASK_NUMS = 10000;

    // 日志信息告警等级
    public static final String LEVEL_INFO = "INFO";
    public static final String LEVEL_ERROR = "ERROR";

}
