package org.javaboy.vhr.service;

import cn.hutool.core.date.TimeInterval;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Service
@Slf4j
public class TestService {
    @Autowired
    UpdateService updateService;
    @Autowired
    AsyncAssetService asyncAssetService;

    public String testImport(MultipartFile file, HttpServletResponse response){
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


}
