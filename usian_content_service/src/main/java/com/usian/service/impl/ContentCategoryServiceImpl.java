package com.usian.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.constant.BigADConstant;
import com.usian.mapper.TbContentCategoryMapper;
import com.usian.mapper.TbContentMapper;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.service.ContentCategoryService;
import com.usian.util.RedisClient;
import com.usian.utils.JsonUtils;
import com.usian.utils.PageResult;
import com.usian.vo.BigADContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    @Autowired
    private TbContentMapper tbContentMapper;
    @Override
    public List<TbContentCategory> selectContentCategoryByParentId(Long parentId) {
        //方式一
//        TbItemCat tbItemCat = new TbItemCat();
//        tbItemCat.setParentId(id);
//        List<TbItemCat> tbItemCatList =  tbItemCatMapper.select(tbItemCat);


        //方式二
        Example example = Example.builder(TbContentCategory.class)
                .where(Sqls.custom().andEqualTo("parentId",parentId).andEqualTo("status",1))
                .build();

        List<TbContentCategory> itemCats = tbContentCategoryMapper.selectByExample(example);

        return itemCats;
    }

    @Transactional
    @Override
    public void insertContentCategory(Long parentId, String name) {
        //新增新节点
        TbContentCategory tbContentCategory = new TbContentCategory();
        Date date = new Date();
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
        tbContentCategory.setStatus(1);
        tbContentCategory.setCreated(date);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setSortOrder(1);
        tbContentCategoryMapper.insertSelective(tbContentCategory);

        //修改父
        //先判断父原来是否为父（查询父的信息）
        TbContentCategory fu = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (!fu.getIsParent()){
            fu.setIsParent(true);
            fu.setUpdated(date);
            tbContentCategoryMapper.updateByPrimaryKeySelective(fu);
        }



        //如果不是父改成父


    }

    @Override
    public void updateContentCategory(Long id, String name) {
        //
        TbContentCategory tbContentCategory = new TbContentCategory();
        Date date = new Date();
        tbContentCategory.setId(id);
        tbContentCategory.setName(name);
        tbContentCategory.setUpdated(date);
        tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
    }

    @Override
    public PageResult selectTbContentAllByCategoryId(Integer page, Integer rows, Long categoryId) {
        PageHelper.startPage(page, rows);
        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("categoryId",categoryId);

       // example.createCriteria().andEqualTo("status",1);
        List<TbContent> list = this.tbContentMapper.selectByExample(example);

        PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
        PageResult result = new PageResult(page,pageInfo.getPages(),pageInfo.getList());

        return result;
    }

    @Override
    public Integer insertTbContent(TbContent tbContent) {
            tbContent.setCreated(new Date());
            return tbContentMapper.insertSelective(tbContent);
    }

    @Override
    public void deleteContentByIds(Long ids) {
        tbContentMapper.deleteByPrimaryKey(ids);
    }

    @Override
    public void deleteContentCategoryById(Long categoryId) {
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(categoryId);
        if (!tbContentCategory.getIsParent()){
            tbContentCategory.setStatus(2);
            tbContentCategory.setUpdated(new Date());
            tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
            Example example = new Example(TbContentCategory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("parentId",tbContentCategory.getParentId());
            criteria.andEqualTo("status",1);
            List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
            if (list == null || list.size() == 0){
                TbContentCategory tbContentCategory1 = new TbContentCategory();
                tbContentCategory1.setId(tbContentCategory.getParentId());
                tbContentCategory1.setIsParent(false);
                tbContentCategory1.setUpdated(new Date());
                tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory1);
            }
        }
    }

    @Override
    public List<BigADContentVo> selectFrontendContentByAD() {

//        Object portal_ad_key = redisClient.hget("PORTAL_AD_KEY", BigAdConstant.CONTENT_CATEGORY_ID.toString());
//        if (portal_ad_key != null){
//            List<ContentBigAD> contentBigADS = JsonUtils.jsonToList(portal_ad_key.toString(), ContentBigAD.class);
//            if (contentBigADS != null) {
//                System.out.println("redis");
//                return contentBigADS;
//            }
//        }
//        QueryWrapper<Content> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("category_id", BigAdConstant.CONTENT_CATEGORY_ID);
//        List<Content> contents = contentMapper.selectList(queryWrapper);
//        List<ContentBigAD> contentBigADS = new ArrayList<>();
//        contents.forEach(content -> {
//            ContentBigAD ad = new ContentBigAD();
//            ad.setHeight(BigAdConstant.CONTENT_HEIGHT);
//            ad.setHeightB(BigAdConstant.CONTENT_HEIGHT_B);
//            ad.setWidth(BigAdConstant.CONTENT_WIDTH);
//            ad.setWidthB(BigAdConstant.CONTENT_WIDTH_B);
//            ad.setSrc(content.getPic());
//            ad.setSrcB(content.getPic2());
//            ad.setAlt(content.getTitle());
//            ad.setHref(content.getUrl());
//            contentBigADS.add(ad);
//        });
//        System.out.println("mysql");
//        redisClient.hset("PORTAL_AD_KEY", BigAdConstant.CONTENT_CATEGORY_ID.toString(), JsonUtils.objectToJson(contentBigADS));
//        return contentBigADS;
//    }


        Object index_category_da = redisClient.hget("INDEX_CATEGORY_DA", BigADConstant.BIG_AD_CATEGORY_ID.toString());
        if (index_category_da!=null){
            List<BigADContentVo> bigADContentVos = JsonUtils.jsonToList(index_category_da.toString(), BigADContentVo.class);
            if (bigADContentVos != null){
                System.out.println("redis");
                return bigADContentVos;
            }
        }
        //.查询打广告的内容    Tb_Content的集合        select * from tb_content where category_id = 89
        TbContent tbContent = new TbContent(); //创建一个查询条件对象
        tbContent.setCategoryId(BigADConstant.BIG_AD_CATEGORY_ID);
        List<TbContent> oldAds = tbContentMapper.select(tbContent);
        //Tb_Content的集合--->BigADContentVo类型
        List<BigADContentVo> newAds = new ArrayList<>();

        oldAds.forEach(oldAd -> {
           BigADContentVo newAd = new BigADContentVo();
           newAd.setHeight(BigADConstant.COMMON_HEIGHT);
           newAd.setHeightB(BigADConstant.COMMON_HEIGHT_B);
           newAd.setWidth(BigADConstant.COMMON_WIDTH);
           newAd.setWidthB(BigADConstant.COMMON_WIDTH_B);
           newAd.setSrc(oldAd.getPic());
           newAd.setSrcB(oldAd.getPic2());
           newAd.setHref(oldAd.getUrl());
           newAd.setAlt(oldAd.getContent());
           newAds.add(newAd);
        });
        System.out.println("mybatis");
        redisClient.hset("INDEX_CATEGORY_DA",BigADConstant.BIG_AD_CATEGORY_ID.toString(),JsonUtils.objectToJson(newAds));
        return newAds;

    }


}
