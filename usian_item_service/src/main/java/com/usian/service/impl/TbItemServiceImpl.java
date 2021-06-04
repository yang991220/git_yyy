package com.usian.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.dto.ItemDto;
import com.usian.mapper.TbItemCatMapper;
import com.usian.mapper.TbItemDescMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemParamItem;
import com.usian.service.TbItemService;
import com.usian.util.RedisClient;
import com.usian.utils.IDUtils;
import com.usian.utils.PageResult;
import com.usian.vo.ItemVo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TbItemServiceImpl implements TbItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private RedisClient redisClient;


    /**
     * 查询商品基本信息
     */
    public TbItem selectItemInfo(Long id) {


        TbItem item = (TbItem) redisClient.get("ITEM_" + id);

        if (item != null) {
            return item;
        }

        //加分布式锁
        if (redisClient.lock("ITEM_LOCK" + id, "")) {//获取到锁
            redisClient.expire("ITEM_LOCK" + id, 2);
            item = tbItemMapper.selectByPrimaryKey(id);
            if (item == null) {  //解决缓存穿透的问题
                item = new TbItem();
                redisClient.set("ITEM_" + id, item);
                redisClient.expire("ITEM_" + id, 2);
            } else {
                redisClient.set("ITEM_" + id, item);
                redisClient.expire("ITEM_" + id, 60 * 60 * 24);
            }

            redisClient.del("ITEM_LOCK" + id);
        }else { //没有获取到锁
            selectItemInfo(id);
        }
        return item;
    }


    public PageResult selectTbItemAllByPage(@RequestParam(name = "page") Integer page, @RequestParam(name = "rows") Integer rows) {


        PageHelper.startPage(page, rows);
        Example example = new Example(TbItem.class);
        example.setOrderByClause("created DESC");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 1);

        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);

        //pageInfo ----->pageResult
        PageResult pageResult = new PageResult();
        pageResult.setResult(pageInfo.getList());
        pageResult.setTotalPage(page);
        pageResult.setPageIndex(pageInfo.getPages());


        return pageResult;
    }
//    Example example = Example.builder(TbItemCat.class)
//            .where(Sqls.custom().andEqualTo("parentId",parentId))
//            .build();
//    List<TbItemCat> itemCats = tbItemCatMapper.selectByExample(example);


    @Transactional
    @Override
    public void insertTbItem(ItemVo itemVo) {

        //item
        Date date = new Date();
        long itemId = IDUtils.genItemId();
        itemVo.setId(itemId);
        itemVo.setCreated(date);
        itemVo.setStatus((byte) 1);
        tbItemMapper.insertSelective(itemVo);
        //itemDesc
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(itemVo.getDesc());
        tbItemDesc.setCreated(date);
        itemDescMapper.insertSelective(tbItemDesc);
        //ItemParamItem
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemVo.getItemParams());
        tbItemParamItem.setCreated(date);
        itemParamItemMapper.insertSelective(tbItemParamItem);


        ItemDto itemDto = new ItemDto();
        itemDto.setTbItem(itemVo);
        itemDto.setTbItemDesc(tbItemDesc);
        TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(itemVo.getCid());
        itemDto.setCategoryName(tbItemCat.getName());
        //新增数据到es
        amqpTemplate.convertAndSend("item_exchange", "item.insert", itemDto);


    }

    @Override
    public Map<String, Object> preUpdateItem(Long itemId) {
        HashMap<String, Object> map = new HashMap<>();
        //根据商品 ID 查询商品
        TbItem item = this.tbItemMapper.selectByPrimaryKey(itemId);
        map.put("item", item);
        //根据商品 ID 查询商品描述
        TbItemDesc itemDesc = this.itemDescMapper.selectByPrimaryKey(itemId);
        map.put("itemDesc", itemDesc.getItemDesc());
        //根据商品 ID 查询商品类目
        TbItemCat itemCat = this.itemCatMapper.selectByPrimaryKey(item.getCid());
        map.put("itemCat", itemCat.getName());
        //根据商品 ID 查询商品规格信息

        Example example = new Example(TbItemParamItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        List<TbItemParamItem> list = this.itemParamItemMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            map.put("itemParamItem", list.get(0).getParamData());
        }
        return map;
    }

    @Override
    public void updateTbItem(ItemVo itemVo) {

        //item商品基本信息
        Date date = new Date();
        itemVo.setUpdated(date);
        itemVo.setPrice(itemVo.getPrice());
        tbItemMapper.updateByPrimaryKeySelective(itemVo);
        //itemDesc
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemVo.getId());
        tbItemDesc.setItemDesc(itemVo.getDesc());
        tbItemDesc.setUpdated(date);
        itemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
        //ItemParamItem
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setParamData(itemVo.getItemParams());
        tbItemParamItem.setUpdated(date);
        tbItemParamItem.setId(itemVo.getId());
        itemParamItemMapper.updateByPrimaryKeySelective(tbItemParamItem);
    }

    @Override
    public void deleteItemById(Long itemId) {
        TbItem tbItem = new TbItem();
        Date date = new Date();
        tbItem.setId(itemId);
        tbItem.setUpdated(date);
        tbItem.setStatus((byte) 3);
        tbItemMapper.updateByPrimaryKeySelective(tbItem);
    }

    @Override
    public TbItemParamItem selectTbItemParamItemByItemId(Integer itemId) {
        Example example = Example.builder(TbItemParamItem.class)
                .where(Sqls.custom().andEqualTo("itemId", itemId))
                .build();
        List<TbItemParamItem> tbItemParamItems = itemParamItemMapper.selectByExample(example);

        if (tbItemParamItems != null && tbItemParamItems.size() > 0) {
            return tbItemParamItems.get(0);
        }
        return null;

    }

    @Override
    public TbItemDesc selectItemDescByItemId(Long itemId) {
        return itemDescMapper.selectByPrimaryKey(itemId);
    }


}
