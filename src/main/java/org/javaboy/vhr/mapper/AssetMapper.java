package org.javaboy.vhr.mapper;


import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.Asset;

import java.util.List;

public interface AssetMapper {
    Asset getAssetById(@Param("id") String id);
    Asset getAssetBySn(@Param("sn") String sn);
    Asset getAssetBySnByVIP(@Param("sn") String sn);
    int updateAssetBySn(@Param("list") List<Asset> list);
}