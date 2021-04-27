package com.lxj.gmall0419.manage.mapper;

import com.lxj.gmall0419.bean.SpuSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr> {

    public List<SpuSaleAttr> selectSpuSaleAttrList(long spuId);

    List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(String skuId, String spuId);
}