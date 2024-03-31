package org.javaboy.vhr.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Asset {
    private String id;
    @ExcelProperty("sn")
    private String sn;
    @ExcelProperty("资产名称")
    private String name;
    @ExcelProperty("资产数量")
    private Integer number;
    @ExcelProperty("资产备注")
    private String remake;
}
