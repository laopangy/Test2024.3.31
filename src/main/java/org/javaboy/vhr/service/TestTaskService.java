package org.javaboy.vhr.service;

import org.javaboy.vhr.base.BaseException;
import org.javaboy.vhr.constants.TaskConstants;
import org.javaboy.vhr.exception.ExceptionCodes;
import org.javaboy.vhr.model.TaskInfo;
import org.javaboy.vhr.utils.LogUtil;
import org.javaboy.vhr.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @className    : TestTaskService
 * @description    : 测试任务服务类
 * @summary    :
 * @history    : ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2022/08/19	1.0.0		sheng.zheng		初版
 */
@Service
public class TestTaskService {
    // 任务管理器
    @Autowired
    private TaskManService taskManService;

    /**
     * @param request : request对象
     * @param params  : 请求参数，形式如下：
     *                {
     *                "repeat"	: 10,	// 重复次数，默认为10，可选
     *                "delay"		: 1000,	// 延时毫秒数，默认为1000，可选
     *                }
     * @methodName        : addAsyncTask
     * @description     : 新增一个异步任务
     * @summary        : 新增测试任务类型的异步任务，
     * 如果处理队列未满，可立即获取任务ID：
     * 根据此任务ID，可以通过调用getTaskInfo，获取任务的处理进度信息；
     * 如果任务处理完毕，任务信息缓存60秒，过期后无法再获取；
     * 如果处理队列已满，返回任务队列已满的失败提示。
     * @return            : JSON对象，形式如下：
     * {
     * "taskId"	: 1,	// 任务ID
     * }
     * @history        : ------------------------------------------------------------------------------
     * date			version		modifier		remarks
     * ------------------------------------------------------------------------------
     * 2022/08/19	1.0.0		sheng.zheng		初版
     */
    public Map<String, Object> addAsyncTask(HttpServletRequest request,
                                            Map<String, Object> params) {
        // 参数校验
        Integer repeat = (Integer) params.get("repeat");
        if (repeat == null) {
            repeat = 10;
        }
        Integer delay = (Integer) params.get("delay");
        if (delay == null) {
            delay = 1000;
        }
        if (repeat <= 0) {
            // 参数错误
            throw new BaseException(ExceptionCodes.ARGUMENTS_ERROR, "repeat");
        }
        if (delay <= 10) {
            // 参数错误
            throw new BaseException(ExceptionCodes.ARGUMENTS_ERROR, "delay");
        }

        // 任务名称
        String taskName = "测试任务";
        // 任务处理对象
        Object procObject = this;
        // 任务执行方法
        Method method = Utility.getMethodByName(this, "testTask");
        // 调用任务管理器，添加任务
        Integer taskId = taskManService.addTask(request, taskName, procObject, method, params);

        // 返回值处理
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskId", taskId);
        return map;
    }

    /**
     * @param request : request对象
     * @param params  : 请求参数，形式如下：
     *                {
     *                "taskId"	: 1,	// 任务ID，必选
     *                }
     * @methodName        : getTaskInfo
     * @description     : 根据任务ID，获取任务信息
     * @summary        : 如果任务ID对应的任务，属于当前用户，则可以有权获取信息，否则拒绝。
     * 如果任务状态为未处理或处理中，可以获取任务信息。
     * 如果任务状态为处理结束，且在缓存到期时间之前，也可以获取任务信息。否则，无法获取任务信息。
     * @return            : JSON对象，形式如下：
     * {
     * "taskId"	: 1,	// 任务ID
     * "procStatus": 1,	// 处理状态，0-未处理，1-处理中，2-处理结束
     * "progress"	: 0.0,	// 处理进度百分比
     * "logList"	: [],	// 处理日志，字符串列表，格式化显示：Time level taskId taskName logInfo
     * "result"	: "",	// 处理结果，字符串类型
     * "resultCode": 0,	// 返回码，0表示操作成功
     * "message"	: "",	// 响应消息
     * }
     * @history        : ------------------------------------------------------------------------------
     * date			version		modifier		remarks
     * ------------------------------------------------------------------------------
     * 2022/08/19	1.0.0		sheng.zheng		初版
     */
    public Map<String, Object> getTaskInfo(HttpServletRequest request,
                                           Map<String, Object> params) {
        // 从请求参数中获取taskId
        Integer taskId = (Integer) params.get("taskId");
        if (taskId == null) {
            throw new BaseException(ExceptionCodes.ARGUMENTS_IS_EMPTY, "taskId");
        }
        // 调用任务管理器的方法，获取任务信息对象
        TaskInfo taskInfo = taskManService.getTaskInfo(request, taskId);

        // 返回值处理
        // 从任务信息对象，筛选一些属性返回
        Map<String, Object> map = new HashMap<String, Object>();
        // 任务ID
        map.put("taskId", taskId);
        // 任务状态
        map.put("procStatus", taskInfo.getProcStatus());
        // 处理结果，如任务未结束，则为null
        map.put("result", taskInfo.getResult());
        // 处理日志
        map.put("logList", taskInfo.getLogList());
        // 处理进度
        map.put("progress", taskInfo.getProgress());
        // 可能的返回码和消息
        map.put("resultCode", taskInfo.getResultCode());
        map.put("message", taskInfo.getMessage());

        return map;
    }

    /**
     * @param taskInfo : 任务信息
     * @methodName        : testTask
     * @description     : 测试任务，异步执行
     * @history        : ------------------------------------------------------------------------------
     * date			version		modifier		remarks
     * ------------------------------------------------------------------------------
     * 2022/08/19	1.0.0		sheng.zheng		初版
     */
    public void testTask(TaskInfo taskInfo) {
        // 开始处理任务
        taskInfo.start();

        // 获取参数
        Map<String, Object> params = (Map<String, Object>) taskInfo.getParams();
        Integer repeat = (Integer) params.get("repeat");
        if (repeat == null) {
            repeat = 10;
        }
        Integer delay = (Integer) params.get("delay");
        if (delay == null) {
            delay = 1000;
        }

        String result = "";

        // 重复n次
        for (int i = 0; i < repeat; i++) {
            taskInfo.addLogInfo(TaskConstants.LEVEL_INFO, "处理步骤" + (i + 1));
            // 显示处理进度
            taskInfo.setProgress((i + 1) * 1.0 / repeat * 100);
            // 延迟delay毫秒
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                LogUtil.error(e);
            }
        }
        result = "OK";

        // 处理完毕
        taskInfo.finish(result);
    }
}
