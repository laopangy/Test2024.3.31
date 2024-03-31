package org.javaboy.vhr.controller;

import org.javaboy.vhr.service.TestService;
import org.javaboy.vhr.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    TestService testService;

    @PostMapping("/testImport")
    public String testImport(@RequestParam("file") MultipartFile file, HttpServletResponse response){
        return testService.testImport(file,response);
    }

}
