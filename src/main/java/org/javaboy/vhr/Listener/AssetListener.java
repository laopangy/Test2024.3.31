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
//        verify(asset);
//        saveData(asset);
//        assetList.add(asset);
//        if (assetList.size()>=count){
//
//        }
        assetYesList.add(asset);


    }





    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
