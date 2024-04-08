package org.javaboy.vhr.service;

import com.alibaba.excel.EasyExcel;
import org.javaboy.vhr.Listener.AssetListener;
import org.javaboy.vhr.model.Asset;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ExcelService {


    public void readImportUpdate(InputStream inputStream, Class tClass, AssetListener assetListener){
        EasyExcel.read(inputStream,tClass,assetListener).sheet().doRead();
    }
}
