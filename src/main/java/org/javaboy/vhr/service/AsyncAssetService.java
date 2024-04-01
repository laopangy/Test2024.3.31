package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.AssetMapper;
import org.javaboy.vhr.model.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class AsyncAssetService {
    @Autowired
    AssetMapper assetMapper;
    @Async("messageServiceExecutor")
    public void testImportAsync(){
        System.out.println("当前线程："+Thread.currentThread().getName());
    }

    @Async("messageServiceExecutor")
    public void testAsync(List<Asset> assetList){ /*HttpServletResponse response*/
        System.out.println("==============异步开始==========================");
        System.out.println("当前线程："+Thread.currentThread().getName());
        /*response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        String fileName = "设备信息导出.xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        try {
            TimeInterval timeInterval = new TimeInterval();
            log.info("开始导出{} ms",timeInterval.intervalRestart());
            ExcelWriter excelWriter = EasyExcelFactory.write(response.getOutputStream())
                    .withTemplate(new ClassPathResource("file/台账数据导出模板.xlsx").getInputStream()).build();
            log.info("获取导出模板{} ms",timeInterval.intervalRestart());
            WriteSheet writeSheet = EasyExcelFactory.writerSheet().build();
            excelWriter.fill(new FillWrapper("equipmentInfoExportVos", assetList), writeSheet);
            excelWriter.finish();
            log.info("导出完成{} ms",timeInterval.intervalRestart());
        }catch (Exception e){
            throw new RuntimeException("文件生成失败");
        }*/
        //生成文件代码

        try {
            Thread.sleep(90000);
            int i = assetMapper.updateAssetBySn(assetList);
            System.out.println("运行结果："+i);
            System.out.println("==========休眠结束=======");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



    }
}
