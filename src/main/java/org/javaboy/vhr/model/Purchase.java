package org.javaboy.vhr.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Purchase {
	@ExcelProperty("物料名称")
	private String materialName;
	@ExcelProperty("数量")
	private Integer count;
	@ExcelProperty("单价")
	private String price;
	@ExcelProperty("备注")
	private String remark;
}
