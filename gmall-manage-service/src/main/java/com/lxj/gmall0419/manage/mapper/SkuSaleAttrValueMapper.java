package com.lxj.gmall0419.manage.mapper;

import com.lxj.gmall0419.bean.SkuSaleAttrValue;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuSaleAttrValueMapper extends Mapper<SkuSaleAttrValue> {

    public List<SkuSaleAttrValue> selectSkuSaleAttrValueListBySpu (String spuId);
}