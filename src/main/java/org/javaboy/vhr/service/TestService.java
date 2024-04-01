package org.javaboy.vhr.service;

import cn.hutool.core.date.TimeInterval;
import com.alibaba.excel.EasyExcel;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.javaboy.vhr.Listener.AssetListener;
import org.javaboy.vhr.model.Asset;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

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

    public void execlUpload(HttpServletResponse response) throws Exception{
        List<Asset> assets = new ArrayList<>();
        Asset a1 = new Asset();
        a1.setSn("1");
        a1.setName("1");
        a1.setRemake("1");
        a1.setNumber(1);
        Asset a2 = new Asset();
        a2.setSn("2");
        a2.setName("2");
        a2.setRemake("2");
        a2.setNumber(2);
        assets.add(a1);
        assets.add(a2);

        //创建一个流，等待写入excel文件内容
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //将excel文件写入byteArrayOutputStream中
        EasyExcel.write(byteArrayOutputStream, Asset.class).sheet("sheet1").doWrite(assets);

        //查询生成的excel文件（byteArrayOutputStream）大小
        System.out.println(byteArrayOutputStream.size());

        //创建inputStream流
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        byte[] bytes = byteArrayOutputStream.toByteArray();
        File file = byteToFile(bytes, "正常数据");
        MultipartFile multipartFilesss = fileToMultipartFile(file);
        try {
            InputStream inputStreamsss = multipartFilesss.getInputStream();
            updateService.readImportUpdate(inputStreamsss, Asset.class, new AssetListener());
            inputStreamsss.close();
//            asyncAssetService.testAsync(assets);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //将inputStream流写成文件（查看文件内容是否正确）
      /*  int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream("C:\\Users\\潘高远\\Desktop\\yxh.xlsx");
        while ((index = inputStream.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }*/



        inputStream.close();
//        downloadFile.close();

    }

    public static File byteToFile(byte[] bytes, String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        try {
            bufferedOutputStream.write(bytes);
        } catch (IOException e) {
            log.error("byte[]转file异常", e);
        } finally {
            try {
                bufferedOutputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                log.error("byte[]转file异常", e);
            }
        }
        return file;
    }

    public static FileItem creatFileItem(File file) {

        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory(16, null);
        FileItem fileItem = diskFileItemFactory.createItem("textField", "application/zip", true, file.getName());

        int bytesRead = 0;
        byte[] buffer = new byte[8192];

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            OutputStream outputStream = fileItem.getOutputStream();
            while ((bytesRead = fileInputStream.read(buffer, 0, 8192)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            Files.copy(file.toPath(), outputStream);
            outputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileItem;
    }
    public static MultipartFile fileToMultipartFile(File file) {
        FileItem fileItem = creatFileItem(file);
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }
}
