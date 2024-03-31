package org.javaboy.vhr.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.javaboy.vhr.config.SpringContextConfig;
import org.javaboy.vhr.mapper.AssetMapper;
import org.javaboy.vhr.model.Asset;
import org.javaboy.vhr.service.AsyncAssetService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class AssetListener extends AnalysisEventListener<Asset> {


    private static final int count = 500;
    private static int i = 0;

    private static List<Asset> assetErrorList = new ArrayList<>();
    private static List<Asset> assetYesList = new ArrayList<>();
    @Override
    public void invoke(Asset asset, AnalysisContext analysisContext) {
        i++;
        //校验
        verify(asset);
        saveData(asset);
//        assetList.add(asset);
//        if (assetList.size()>=count){
//
//        }
        System.out.println(i);


    }


    //校验数据
    private void verify(Asset asset){
        //验证sn是否为空
        if (Objects.isNull(asset.getSn())){
            throw new RuntimeException("文件中第"+i+"行sn为空");
        }else if (Objects.isNull(asset.getName())){
            throw new RuntimeException("文件中第"+i+"行资产名称为空");
        }if (Objects.isNull(asset.getNumber())){
            throw new RuntimeException("文件中第"+i+"行资产数量为空");
        }/*if (Objects.isNull(asset.getRemak())){
            throw new RuntimeException("文件中第"+i+"行资产备注为空");
        }*/
    }

    private void saveData(Asset asset){
        AssetMapper assetMapper = SpringContextConfig.getBean(AssetMapper.class);
        Asset assetBySn = assetMapper.getAssetBySn(asset.getSn());
        if (Objects.isNull(assetBySn)){
            assetErrorList.add(asset);//错误信息
            return;
        }
        assetYesList.add(asset);
    }

    private void AsyncExeclUpload(List<Asset> assets){
        System.out.println("当前线程："+Thread.currentThread().getName());
        AsyncAssetService bean = SpringContextConfig.getBean(AsyncAssetService.class);
        bean.testAsync(assets);
//        assetYesList.clear();
    }

//    private void ssExeclUpload(List<Asset> assets){
//        AssetMapper bean = SpringContextConfig.getBean(AssetMapper.class);
//        bean.updateAssetBySn(assets);
//    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println(1);
//        ssExeclUpload(assetYesList);
        AsyncExeclUpload(assetYesList);
        assetErrorList.clear();
//        assetYesList.clear();
    }
}
