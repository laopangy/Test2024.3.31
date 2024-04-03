package org.javaboy.vhr.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Asset {
    @ExcelProperty("SN")
    private String sn;
    @ExcelProperty("关联机器SN")
    private String relevanceSn;
    @ExcelProperty("物料名称")
    private String materialName;
    @ExcelProperty("站点")
    private String site;
    @ExcelProperty("公司主体")
    private String companyMain;
    @ExcelProperty("数量")
    private Integer count;
    @ExcelProperty("入库原因")
    private String storageReason;
}
