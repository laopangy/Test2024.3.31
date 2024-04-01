package org.javaboy.vhr.model;

import lombok.Data;
import org.javaboy.vhr.constants.TaskConstants;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//任务信息
@Data
public class TaskInfo {

    // ////////////////////////////////////////////////
    // 任务识别信息

    // 任务ID
    private Integer taskId = 0;

    // sessionId，用于识别请求者
    private String sessionId = "";

    // 任务名称，即业务处理的名称，如查询商品最低价，导入学员名册
    private String taskName = "";

    // ////////////////////////////////////////////////
    // 任务执行相关的

    // 请求参数，使用字典进行封装，以便适应任意数据结构
    private Map<String, Object> params;

    // 处理对象，一般是service对象
    private Object procObject;

    // 处理方法
    private Method method;

    // ////////////////////////////////////////////////
    // 任务处理产生的数据，中间数据，结果

    // 处理状态，0-未处理，1-处理中，2-处理结束
    private int procStatus = 0;

    // 处理结果，数据类型由业务单元约定
    private Object result;

    // 处理日志，包括中间结果，格式化显示：Time level taskId taskName logInfo
    private List<String> logList = new ArrayList<String>();

    // 处理进度百分比
    private double progress = 0;

    // 到期时间，UTC，任务完成后才设置，超时后销毁
    private long expiredTime = 0;

    // 返回码，保留，0表示操作成功
    private int resultCode = 0;

    // 响应消息，保留
    private String message = "";

    // 开始处理时间，便于统计任务处理时长
    private long startTime = 0;

    // ////////////////////////////////////////////////
    // 日志相关的方法
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // 添加处理日志
    public void addLogInfo(String level, String logInfo) {
        // 格式化显示：Time level taskId taskName logInfo
        LocalDateTime current = LocalDateTime.now();
        String strCurrent = current.format(df);
        String log = String.format("%s %s %d %s --- %s",
                strCurrent, level, taskId, taskName, logInfo);
        logList.add(log);
    }


    // ////////////////////////////////////////////////
    // 不同状态的参数设置接口

    // 设置任务初始化，未开始
    public void init(Integer taskId, String taskName, String sessionId,
                     Object procObject, Method method, Map<String, Object> params) {
        this.procStatus = 0;
        this.taskId = taskId;
        this.taskName = taskName;
        this.sessionId = sessionId;
        this.procObject = procObject;
        this.method = method;
        this.params = params;
    }

    // 启动任务
    public void start() {
        this.procStatus = 1;
        addLogInfo(TaskConstants.LEVEL_INFO, "开始处理任务...");
        // 记录任务开始处理的时间
        startTime = System.currentTimeMillis();
    }

    // 结束任务
    public void finish(Object result) {
        this.result = result;
        this.procStatus = 2;
        // 设置结果缓存的到期时间
        expired();
    }

    // 处理异常
    public void error(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
        this.procStatus = 2;
        // 设置结果缓存的到期时间
        expired();
    }

    // 设置过期过期
    public void expired() {
        long current = System.currentTimeMillis();
        this.expiredTime = current + TaskConstants.PROC_EXPIRE_TIME;
        long duration = 0;
        double second = 0.0;
        duration = current - startTime;
        second = duration / 1000.0;
        addLogInfo(TaskConstants.LEVEL_INFO, "任务处理结束，耗时(s):" + second);
    }
}
