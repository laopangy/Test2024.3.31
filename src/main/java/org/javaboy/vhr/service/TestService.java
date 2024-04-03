package org.javaboy.vhr.service;

import cn.hutool.core.date.TimeInterval;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import lombok.extern.slf4j.Slf4j;
import org.javaboy.vhr.Listener.AssetListener;
import org.javaboy.vhr.mapper.AssetMapper;
import org.javaboy.vhr.model.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TestService {
    @Autowired
    UpdateService updateService;
    @Autowired
    AsyncAssetService asyncAssetService;

    public String testImport(MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            updateService.readImportUpdate(inputStream, Asset.class, new AssetListener());
            inputStream.close();
//            asyncAssetService.testAsync(assets);
            return "yes";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void easyExeclTest( HttpServletResponse response){
        //调用业务，构造模板列表数据
        List<Asset> listModel = new ArrayList<>();
        Asset a1 = new Asset();
        a1.setSn("1");
        a1.setRelevanceSn("1");
        a1.setCompanyMain("1");
        a1.setCount(1);
        a1.setStorageReason("1");
        a1.setMaterialName("1");
        a1.setSite("1");
        Asset a2 = new Asset();
        a2.setSn("2");
        a2.setRelevanceSn("2");
        a2.setCompanyMain("2");
        a2.setCount(2);
        a2.setStorageReason("2");
        a2.setMaterialName("2");
        a2.setSite("2");
        listModel.add(a1);
        listModel.add(a2);


        // 重要! 设置返回格式是excel形式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // 设置编码格式
        response.setCharacterEncoding("utf-8");
        // 设置URLEncoder.encode 防止中文乱码
        String fileName = null;
        try {
            fileName = URLEncoder.encode("数据批量导出", "UTF-8").replaceAll("\\+", "%20");
            // 设置响应头
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");



            // 模板文件保存在springboot项目的resources/static下
            Resource resource = new ClassPathResource("static/"+fileName);

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream())
                    .withTemplate(resource.getInputStream())	// 利用模板的输出流
                    .build();
            // 写入模板文件的第一个sheet 索引0
            WriteSheet writeSheet = EasyExcel.writerSheet(0).build();

            // 将数据写入到模板文件的对应sheet中
            excelWriter.write(listModel, writeSheet);
            excelWriter.finish();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
