package org.javaboy.vhr.controller;

import org.javaboy.vhr.base.BaseController;
import org.javaboy.vhr.base.BaseResponse;
import org.javaboy.vhr.service.TestTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @className    : AsycTestController
 * @description    : 异步任务测试控制器类
 * @summary    :
 * @history    : ------------------------------------------------------------------------------
 * date			version		modifier		remarks
 * ------------------------------------------------------------------------------
 * 2022/08/19	1.0.0		sheng.zheng		初版
 */
@RequestMapping("/asycTest")
@RestController
public class AsycTestController extends BaseController {
    @Autowired
    private TestTaskService tts;

    @RequestMapping("/addTask")
    public BaseResponse<Map<String, Object>> addTask(@RequestParam("file") MultipartFile file,HttpServletRequest request) {

        Map<String, Object> map = tts.addAsyncTask(file,request);
        return successResponse(map);
    }

    @RequestMapping("/getTaskInfo")
    public BaseResponse<Map<String, Object>> getTaskInfo(HttpServletRequest request,
                                                         @RequestBody Map<String, Object> params) {
        Map<String, Object> map = tts.getTaskInfo(request, params);
        return successResponse(map);
    }
}
