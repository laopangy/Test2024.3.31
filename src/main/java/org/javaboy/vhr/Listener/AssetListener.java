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



    private List<Asset> assetList = new ArrayList<>();
    @Override
    public void invoke(Asset asset, AnalysisContext analysisContext) {
        if (!Objects.isNull(asset)){
            assetList.add(asset);
        }
    }
    /**
     * 返回数据
     *
     * @return 返回读取的数据集合
     **/
    public List<Asset> getDatas() {
        return assetList;
    }

    /**
     * 设置读取的数据集合
     *
     * @param data 设置读取的数据集合
     **/
    public void setData(List<Asset> data) {
        this.assetList = data;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
