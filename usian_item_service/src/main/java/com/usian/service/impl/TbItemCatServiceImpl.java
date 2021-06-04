package com.usian.service.impl;

import com.usian.mapper.TbItemCatMapper;
import com.usian.pojo.TbItemCat;
import com.usian.service.TbItemCatService;
import com.usian.util.RedisClient;
import com.usian.vo.ItemCategoryDataVo;
import com.usian.vo.ItemCategoryDefVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.List;

@Service
public class TbItemCatServiceImpl implements TbItemCatService {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;
        //查询类目
    public List<TbItemCat> selectItemCategoryByParentId(long parentId) {
        //方式一
//        TbItemCat tbItemCat = new TbItemCat();
//        tbItemCat.setParentId(id);
//        List<TbItemCat> tbItemCatList =  tbItemCatMapper.select(tbItemCat);


        //方式二
        Example example = Example.builder(TbItemCat.class)
                .where(Sqls.custom().andEqualTo("parentId",parentId))
                .build();
        List<TbItemCat> itemCats = tbItemCatMapper.selectByExample(example);

        return itemCats;


    }

    //组装所有分类信息的核心代码
    @Override
    public ItemCategoryDataVo selectItemCategoryAll() {
        //先去缓存中找
        ItemCategoryDataVo list = (ItemCategoryDataVo) redisClient.get("INDEX_CATEGORY_LIST");
        if (list!=null){
            return list;
        }
        //没有了  去数据库找
        //查到了  存到redis，并返回





        ItemCategoryDataVo dataVo = new ItemCategoryDataVo();
        //查询所有一级分类的集合

        dataVo.setData(selectItemCategoryDefVoByParentId(0L)); //所有的一级分类  集合的个数？
        //存到redis
        return dataVo;
    }


    //查询一级的分类

    public List<ItemCategoryDefVo> selectItemCategoryDefVoByParentId(Long parentId){
        List<TbItemCat> tbItemCatList = selectItemCategoryByParentId(parentId);
        List  itemNewCats = new ArrayList<>();//新分类  类型的集合
        //将旧类型 Item  --> ItemCategoryDefVo
        for (TbItemCat tbItemCat : tbItemCatList) {   //某一个分类
            //判断当前节点是否为叶子节点
            if (!tbItemCat.getIsParent()){
                itemNewCats.add(tbItemCat.getName());
            }else {
                ItemCategoryDefVo defVo = new ItemCategoryDefVo();
                defVo.setN(tbItemCat.getName());  // name ---> n
                //查找当前这个分类下的所有的子类
                List<ItemCategoryDefVo> itemCategoryDefVos = selectItemCategoryDefVoByParentId(tbItemCat.getId());
                defVo.setI(itemCategoryDefVos);
                itemNewCats.add(defVo);
            }

        }

        return itemNewCats;
    }
}
