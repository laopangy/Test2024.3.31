package org.javaboy.vhr.service;

import org.javaboy.vhr.base.BaseException;
import org.javaboy.vhr.constants.TaskConstants;
import org.javaboy.vhr.exception.ExceptionCodes;
import org.javaboy.vhr.model.TaskInfo;
import org.javaboy.vhr.utils.LogUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @className    : TaskManService
 * @description    : 任务管理器
 * @summary    :
 * @history    : ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2022/08/18	1.0.0		sheng.zheng		初版
 */
@Service
public class TaskManService {
    // 任务队列，考虑OOM(Out Of Memory)问题，限定任务队列长度，相当于二级缓存
    private BlockingQueue<TaskInfo> taskQueue =
            new LinkedBlockingQueue<TaskInfo>(TaskConstants.MAX_TASK_NUMS);

    // 任务信息字典，key为taskId，目的是为了方便根据taskId查询任务信息
    private Map<Integer, TaskInfo> taskMap = new HashMap<Integer, TaskInfo>();

    // 线程池，工作线程队列长度为线程池的最大线程数，相当于一级缓存
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
            TaskConstants.CORE_POOL_SIZE,
            TaskConstants.MAX_POOL_SIZE,
            TaskConstants.KEEP_ALIVE_SECONDS,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(TaskConstants.MAX_POOL_SIZE),
            Executors.defaultThreadFactory());

    // 任务ID计数器，累加
    private AtomicInteger taskIdCounter = new AtomicInteger();

    // 用于缓存上次检查时间
    private long lastTime = 0;

    // 监视线程，用于任务调度，以及检查已结束任务的缓存到期时间
    private Thread monitor;

    @PostConstruct
    public void init() {
        // 启动线程实例
        monitor = new Thread(checkRunnable);
        monitor.start();

        // 启动一个核心线程
        executor.prestartCoreThread();
    }

    // 检查已结束任务的缓存到期时间，超期的销毁
    private Runnable checkRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                long current = System.currentTimeMillis();
                if (current - lastTime >= 1000) {
                    // 离上次检查时间超过1秒
                    checkAndremove();
                    // 更新lastTime
                    lastTime = current;
                }
                synchronized (this) {
                    try {
                        // 检查任务队列
                        if (taskQueue.isEmpty()) {
                            // 如果任务队列为空，则等待100ms
                            Thread.sleep(100);
                        } else {
                            // 如果任务队列不为空
                            // 检查线程池队列
                            if (executor.getQueue().size() < TaskConstants.MAX_POOL_SIZE) {
                                // 如果线程池队列未满
                                // 从任务队列中获取一个任务
                                TaskInfo taskInfo = taskQueue.take();
                                // 创建Runnable对象
                                TaskRunnable tr = new TaskRunnable(taskInfo);
                                // 调用线程池执行任务
                                executor.execute(tr);
                            } else {
                                // 如果线程池队列已满，则等待100ms
                                Thread.sleep(100);
                            }
                        }
                    } catch (InterruptedException e) {
                        LogUtil.error(e);
                    }
                }
            }
        }
    };

    /**
     * @methodName    : checkAndremove
     * @description    : 检查并移除过期对象
     * @history    : ------------------------------------------------------------------------------
     * date			version		modifier		remarks
     * ------------------------------------------------------------------------------
     * 2022/08/15	1.0.0		sheng.zheng		初版
     */
    private void checkAndremove() {
        synchronized (taskMap) {
            if (taskMap.size() == 0) {
                // 如果无对象
                return;
            }
            long current = System.currentTimeMillis();
            Iterator<Map.Entry<Integer, TaskInfo>> iter = taskMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Integer, TaskInfo> entry = iter.next();
                TaskInfo taskInfo = entry.getValue();
                long expiredTime = taskInfo.getExpiredTime();
                if ((expiredTime != 0) && ((current - expiredTime) > TaskConstants.PROC_EXPIRE_TIME)) {
                    // 如果过期，移除
                    iter.remove();
                }
            }
        }
    }

    /**
     * @param request    : request对象
     * @param taskName   : 任务名称
     * @param procObject : 处理对象
     * @param method     : 处理方法
     * @param params     : 方法参数，透明传递到处理方法中
     * @methodName        : addTask
     * @description : 添加任务
     * @return        : 处理ID，唯一标识该请求的处理
     * @history        : ------------------------------------------------------------------------------
     * date			version		modifier		remarks
     * ------------------------------------------------------------------------------
     * 2022/08/19	1.0.0		sheng.zheng		初版
     */
    public Integer addTask(HttpServletRequest request,
                           String taskName, Object procObject, Method method,
                           Map<String, Object> params) {
        // 获取sessionId
        String sessionId = null;
        if (request.getSession() != null) {
            sessionId = request.getSession().getId();
        } else {
            // 无效的session
            throw new BaseException(ExceptionCodes.SESSION_IS_NULL);
        }

        // 空指针保护
        if (procObject == null) {
            throw new BaseException(ExceptionCodes.ARGUMENTS_ERROR, "procObject对象为null");
        }
        if (method == null) {
            throw new BaseException(ExceptionCodes.ARGUMENTS_ERROR, "method对象为null");
        }
        if (params == null) {
            throw new BaseException(ExceptionCodes.ARGUMENTS_ERROR, "params对象为null");
        }

        // 获取可用的任务ID
        Integer taskId = taskIdCounter.incrementAndGet();

        // 生成任务处理信息对象
        TaskInfo item = new TaskInfo();
        // 初始化任务信息
        item.init(taskId, taskName, sessionId, procObject, method, params);

        // 加入处理队列
        try {
            synchronized (taskQueue) {
                taskQueue.add(item);
            }
        } catch (IllegalStateException e) {
            // 队列已满
            throw new BaseException(ExceptionCodes.ADD_OBJECT_FAILED, "任务队列已满");
        }

        // 加入字典
        synchronized (taskMap) {
            taskMap.put(taskId, item);
        }

        return taskId;
    }

    /**
     * @param request : request对象
     * @param taskId  : 任务ID
     * @methodName        : getTaskInfo
     * @description     : 获取任务信息
     * @return        : TaskInfo对象
     * @history        : ------------------------------------------------------------------------------
     * date			version		modifier		remarks
     * ------------------------------------------------------------------------------
     * 2022/08/19	1.0.0		sheng.zheng		初版
     */
    public TaskInfo getTaskInfo(HttpServletRequest request, Integer taskId) {
        TaskInfo item = null;
        synchronized (taskMap) {
            if (taskMap.containsKey(taskId)) {
                item = taskMap.get(taskId);
                String sessionId = request.getSession().getId();
                if (!sessionId.equals(item.getSessionId())) {
                    throw new BaseException(ExceptionCodes.TASKID_NOT_RIGHTS);
                }
            } else {
                throw new BaseException(ExceptionCodes.TASKID_NOT_EXIST);
            }
        }

        return item;
    }

}
