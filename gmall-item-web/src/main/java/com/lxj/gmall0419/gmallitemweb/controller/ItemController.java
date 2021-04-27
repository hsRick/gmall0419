package com.lxj.gmall0419.gmallitemweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxj.gmall0419.bean.SkuInfo;
import com.lxj.gmall0419.bean.SpuSaleAttr;
import com.lxj.gmall0419.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ItemController {
    @Reference
    private ManageService manageService;

    @RequestMapping("/{skuId}.html")
    public String getSkuInfo(@PathVariable("skuId") String skuId, Model model){
        // 存储基本的skuInfo信息
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        model.addAttribute("skuInfo",skuInfo);
        // 存储 spu，sku数据
        List<SpuSaleAttr> saleAttrList = manageService.getSpuSaleAttrListCheckBySku(skuInfo);
        model.addAttribute("saleAttrList",saleAttrList);
        return "item";
    }

    @RequestMapping("{skuId}.html")
    public String skuInfoPage(@PathVariable(value = "skuId") String skuId, Model model){
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        model.addAttribute("skuInfo",skuInfo);
        return "item";
    }
}