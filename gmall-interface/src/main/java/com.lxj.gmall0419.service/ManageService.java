package com.lxj.gmall0419.service;

import com.lxj.gmall0419.attr.BaseAttrInfo;
import com.lxj.gmall0419.attr.BaseSaleAttr;
import com.lxj.gmall0419.bean.SpuInfo;
import com.lxj.gmall0419.catalog.BaseCatalog1;
import com.lxj.gmall0419.catalog.BaseCatalog2;
import com.lxj.gmall0419.catalog.BaseCatalog3;

import java.util.List;

public interface ManageService {

public List<BaseCatalog1> getCatalog1();

public List<BaseCatalog2> getCatalog2(String catalog1Id);

public List<BaseCatalog3> getCatalog3(String catalog2Id);

public List<BaseAttrInfo> getAttrList(String catalog3Id);

    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    BaseAttrInfo getAttrInfo(String attrId);

    List<SpuInfo> getSpuInfoList(SpuInfo spuInfo);

    // 查询基本销售属性表
    List<BaseSaleAttr> getBaseSaleAttrList();
    public void saveSpuInfo(SpuInfo spuInfo);

}