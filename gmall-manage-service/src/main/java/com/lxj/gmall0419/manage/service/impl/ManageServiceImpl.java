package com.lxj.gmall0419.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lxj.gmall0419.attr.BaseAttrInfo;
import com.lxj.gmall0419.attr.BaseAttrValue;
import com.lxj.gmall0419.attr.BaseSaleAttr;
import com.lxj.gmall0419.bean.*;
import com.lxj.gmall0419.catalog.BaseCatalog1;
import com.lxj.gmall0419.catalog.BaseCatalog2;
import com.lxj.gmall0419.catalog.BaseCatalog3;
import com.lxj.gmall0419.config.RedisUtil;
import com.lxj.gmall0419.manage.ManageConst;
import com.lxj.gmall0419.manage.mapper.*;
import com.lxj.gmall0419.service.ManageService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    BaseCatalog1Mapper baseCatalog1Mapper;

    @Autowired
    BaseCatalog2Mapper baseCatalog2Mapper;

    @Autowired
    BaseCatalog3Mapper baseCatalog3Mapper;

    @Autowired
    private SpuInfoMapper spuInfoMapper;
    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;
    @Autowired
    private SpuImageMapper spuImageMapper;
    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;
    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<BaseCatalog1> getCatalog1() {
        List<BaseCatalog1> baseCatalog1List = baseCatalog1Mapper.selectAll();
        return baseCatalog1List;
    }

    @Override
    public List<BaseCatalog2> getCatalog2(String catalog1Id) {
        BaseCatalog2 baseCatalog2 = new BaseCatalog2();
        baseCatalog2.setCatalog1Id(catalog1Id);

        List<BaseCatalog2> baseCatalog2List = baseCatalog2Mapper.select(baseCatalog2);
        return baseCatalog2List;
    }

    @Override
    public List<BaseCatalog3> getCatalog3(String catalog2Id) {
        BaseCatalog3 baseCatalog3;
        baseCatalog3 = new BaseCatalog3();
        baseCatalog3.setCatalog2Id(catalog2Id);

        List<BaseCatalog3> baseCatalog3List = baseCatalog3Mapper.select(baseCatalog3);
        return baseCatalog3List;
    }

    @Override
    public List<BaseAttrInfo> getAttrList(String catalog3_id) {
        /*BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3_id);

        List<BaseAttrInfo> baseAttrInfoList = baseAttrInfoMapper.select(baseAttrInfo);
*/
        List<BaseAttrInfo> baseAttrInfoList = baseAttrInfoMapper.getBaseAttrInfoListByCatalog3Id(Long.parseLong(catalog3_id));
        return baseAttrInfoList;
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        //??????????????????????????????????????????????????????
        if (baseAttrInfo.getId() != null && baseAttrInfo.getId().length() > 0) {
            baseAttrInfoMapper.updateByPrimaryKey(baseAttrInfo);
        } else {

            baseAttrInfo.setId(null);
            baseAttrInfoMapper.insertSelective(baseAttrInfo);
        }
        //???????????????????????????
        BaseAttrValue baseAttrValue4Del = new BaseAttrValue();
        baseAttrValue4Del.setAttrId(baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrValue4Del);

        //?????????????????????
        if (baseAttrInfo.getAttrValueList() != null && baseAttrInfo.getAttrValueList().size() > 0) {
            for (BaseAttrValue attrValue : baseAttrInfo.getAttrValueList()) {
                //???????????????????????????????????????
                attrValue.setId(null);
                attrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insertSelective(attrValue);
            }
        }
    }

    @Override
    public BaseAttrInfo getAttrInfo(String attrId) {
        // ??????????????????
        BaseAttrInfo attrInfo = baseAttrInfoMapper.selectByPrimaryKey(attrId);
        // ?????????????????????
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        // ??????attrId??????????????????
        baseAttrValue.setAttrId(attrInfo.getId());
        List<BaseAttrValue> attrValueList = baseAttrValueMapper.select(baseAttrValue);
        // ??????????????????????????????????????????
        attrInfo.setAttrValueList(attrValueList);
        // ?????????????????????
        return attrInfo;
    }

    @Override
    public List<SpuInfo> getSpuInfoList(SpuInfo spuInfo) {
        return spuInfoMapper.select(spuInfo);
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectAll();
    }

    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        // ??????????????????????????????????????????????????? spuInfo
        if (spuInfo.getId() == null || spuInfo.getId().length() == 0) {
            //????????????
            spuInfo.setId(null);
            spuInfoMapper.insertSelective(spuInfo);
        } else {
            spuInfoMapper.updateByPrimaryKeySelective(spuInfo);
        }

        //  spuImage ???????????? ?????????????????????
        //  delete from spuImage where spuId =?
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuInfo.getId());
        spuImageMapper.delete(spuImage);

        // ??????????????????????????????
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if (spuImageList != null && spuImageList.size() > 0) {
            // ????????????
            for (SpuImage image : spuImageList) {
                image.setId(null);
                image.setSpuId(spuInfo.getId());
                spuImageMapper.insertSelective(image);
            }
        }
        // ???????????? ???????????????
        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuInfo.getId());
        spuSaleAttrMapper.delete(spuSaleAttr);

        // ??????????????? ???????????????
        SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
        spuSaleAttrValue.setSpuId(spuInfo.getId());
        spuSaleAttrValueMapper.delete(spuSaleAttrValue);

        // ????????????
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if (spuSaleAttrList != null && spuSaleAttrList.size() > 0) {
            // ????????????
            for (SpuSaleAttr saleAttr : spuSaleAttrList) {
                saleAttr.setId(null);
                saleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insertSelective(saleAttr);

                // ?????????????????????
                List<SpuSaleAttrValue> spuSaleAttrValueList = saleAttr.getSpuSaleAttrValueList();
                if (spuSaleAttrValueList != null && spuSaleAttrValueList.size() > 0) {
                    // ????????????
                    for (SpuSaleAttrValue saleAttrValue : spuSaleAttrValueList) {
                        saleAttrValue.setId(null);
                        saleAttrValue.setSpuId(spuInfo.getId());
                        spuSaleAttrValueMapper.insertSelective(saleAttrValue);
                    }
                }
            }
        }
    }

    @Override
    public List<SpuImage> getSpuImageList(String spuId) {
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuId);
        return spuImageMapper.select(spuImage);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrList(Long.parseLong(spuId));
    }

    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        // sku_info
        if (skuInfo.getId() == null || skuInfo.getId().length() == 0) {
            // ??????id ?????????
            skuInfo.setId(null);
            skuInfoMapper.insertSelective(skuInfo);
        } else {
            skuInfoMapper.updateByPrimaryKeySelective(skuInfo);
        }

        //        sku_img,
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuInfo.getId());
        skuImageMapper.delete(skuImage);

        // insert
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if (skuImageList != null && skuImageList.size() > 0) {
            for (SkuImage image : skuImageList) {
                /* "" ?????? null*/
                if (image.getId() != null && image.getId().length() == 0) {
                    image.setId(null);
                }
                // skuId ????????????
                image.setSkuId(skuInfo.getId());
                skuImageMapper.insertSelective(image);
            }
        }
//        sku_attr_value,
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuInfo.getId());
        skuAttrValueMapper.delete(skuAttrValue);

        // ????????????
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if (skuAttrValueList != null && skuAttrValueList.size() > 0) {
            for (SkuAttrValue attrValue : skuAttrValueList) {
                if (attrValue.getId() != null && attrValue.getId().length() == 0) {
                    attrValue.setId(null);
                }
                // skuId
                attrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insertSelective(attrValue);
            }
        }
//        sku_sale_attr_value,
        SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
        skuSaleAttrValue.setSkuId(skuInfo.getId());
        skuSaleAttrValueMapper.delete(skuSaleAttrValue);
//      ????????????
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if (skuSaleAttrValueList != null && skuSaleAttrValueList.size() > 0) {
            for (SkuSaleAttrValue saleAttrValue : skuSaleAttrValueList) {
                if (saleAttrValue.getId() != null && saleAttrValue.getId().length() == 0) {
                    saleAttrValue.setId(null);
                }
                // skuId
                saleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValueMapper.insertSelective(saleAttrValue);
            }
        }
    }

    @Override
    public SkuInfo getSkuInfo(String skuId) {
        SkuInfo skuInfo = null;
        try {
            Jedis jedis = redisUtil.getJedis();
            // ??????key
            String skuInfoKey = ManageConst.SKUKEY_PREFIX + skuId + ManageConst.SKUKEY_SUFFIX; //key= sku:skuId:info

            String skuJson = jedis.get(skuInfoKey);

            if (skuJson == null || skuJson.length() == 0) {
                // ???????????? ,????????????????????????????????????????????????????????????????????????????????????????????????
                System.out.println("??????????????????");
                // ??????key user:userId:lock
                String skuLockKey = ManageConst.SKUKEY_PREFIX + skuId + ManageConst.SKULOCK_SUFFIX;
                // ?????????
                String lockKey = jedis.set(skuLockKey, "OK", "NX", "PX", ManageConst.SKULOCK_EXPIRE_PX);
                if ("OK".equals(lockKey)) {
                    System.out.println("????????????");
                    // ???????????????????????????
                    skuInfo = getSkuInfoDB(skuId);
                    // ????????????????????????
                    // ???????????????????????????
                    String skuRedisStr = JSON.toJSONString(skuInfo);
                    jedis.setex(skuInfoKey, ManageConst.SKUKEY_TIMEOUT, skuRedisStr);
                    jedis.close();
                    return skuInfo;
                } else {
                    System.out.println("?????????");
                    // ??????
                    Thread.sleep(1000);
                    // ??????
                    return getSkuInfo(skuId);
                }
            } else {
                // ?????????
                skuInfo = JSON.parseObject(skuJson, SkuInfo.class);
                jedis.close();
                return skuInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ????????????????????????
        return getSkuInfoDB(skuId);
    }


    private SkuInfo getSkuInfoRedisson(String skuId) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.67.219:6379");

        RedissonClient redissonClient = Redisson.create(config);

        // ??????redisson ??????getLock
        RLock lock = redissonClient.getLock("yourLock");

        // ??????
        lock.lock(10, TimeUnit.SECONDS);

//        try {
//            boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // ????????????????????????
        SkuInfo skuInfo = null;
        Jedis jedis = null;
        // ctrl+alt+t
        try {
            jedis = redisUtil.getJedis();
            // ??????key??? ??????????????? sku???skuId:info
            String skuKey = ManageConst.SKUKEY_PREFIX + skuId + ManageConst.SKUKEY_SUFFIX;
            // ???????????????????????????????????????????????????????????????????????????db?????????????????????????????????
            // ??????redis ????????????key
            if (jedis.exists(skuKey)) {
                // ??????key ??????value
                String skuJson = jedis.get(skuKey);
                // ???????????????????????????
                skuInfo = JSON.parseObject(skuJson, SkuInfo.class);
//                jedis.close();
                return skuInfo;
            } else {
                skuInfo = getSkuInfoDB(skuId);
                // ???redis ?????????????????????
                jedis.setex(skuKey, ManageConst.SKUKEY_TIMEOUT, JSON.toJSONString(skuInfo));
                return skuInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
            lock.unlock();
        }
        return getSkuInfoDB(skuId);
    }


    public SkuInfo getSkuInfoDB(String skuId) {
        // ???????????????
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        // ????????????
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId);
        List<SkuImage> imageList = skuImageMapper.select(skuImage);
// ???????????????????????????????????????
        skuInfo.setSkuImageList(imageList);
        // ???????????????
        SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
        skuSaleAttrValue.setSkuId(skuId);
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuSaleAttrValueMapper.select(skuSaleAttrValue);

        // ????????????????????????????????????????????????
        skuInfo.setSkuSaleAttrValueList(skuSaleAttrValueList);
        return skuInfo;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(SkuInfo skuInfo) {
        return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(skuInfo.getId(), skuInfo.getSpuId());
    }
}