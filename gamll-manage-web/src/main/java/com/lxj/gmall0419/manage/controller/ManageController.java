package com.lxj.gmall0419.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxj.gmall0419.attr.BaseAttrInfo;
import com.lxj.gmall0419.attr.BaseAttrValue;
import com.lxj.gmall0419.attr.BaseSaleAttr;
import com.lxj.gmall0419.catalog.BaseCatalog1;
import com.lxj.gmall0419.catalog.BaseCatalog2;
import com.lxj.gmall0419.catalog.BaseCatalog3;
import com.lxj.gmall0419.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
public class ManageController {

    @Reference
    private ManageService manageService;

    @RequestMapping("getCatalog1")
    @ResponseBody
    public List<BaseCatalog1> getCatalog1() {
        return manageService.getCatalog1();
    }

    @RequestMapping("getCatalog2")
    @ResponseBody
    public List<BaseCatalog2> getCatalog2(String catalog1Id) {
        return manageService.getCatalog2(catalog1Id);
    }

    @RequestMapping("getCatalog3")
    @ResponseBody
    public List<BaseCatalog3> getCatalog3(String catalog2Id) {
        return manageService.getCatalog3(catalog2Id);
    }

    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<BaseAttrInfo> attrInfoList(String catalog3Id) {

        return manageService.getAttrList(catalog3Id);
    }

    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public void saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo) {
        // 调用服务层做保存方法
        manageService.saveAttrInfo(baseAttrInfo);
    }

    @RequestMapping(value = "getAttrValueList", method = RequestMethod.POST)
    @ResponseBody
    public List<BaseAttrValue> getAttrValueList(String attrId) {
        BaseAttrInfo attrInfo = manageService.getAttrInfo(attrId);
        return attrInfo.getAttrValueList();
    }

    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return manageService.getBaseSaleAttrList();
    }


}
