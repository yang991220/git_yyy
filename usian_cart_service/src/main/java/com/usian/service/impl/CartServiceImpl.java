package com.usian.service.impl;

import com.usian.mapper.TbItemMapper;
import com.usian.pojo.TbItem;
import com.usian.service.CartService;
import com.usian.util.RedisClient;
import com.usian.vo.ItemCartTermVo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 加入购物车
     * @param userId
     * @param itemId
     */
    @Override
    public void addItem(Long userId, Long itemId) {
        //判断用户是否是第一次使用购物车
        Boolean exists = redisClient.exists("CART" + userId);
        if (exists){
            //不是第一次使用购物车
            //判断该商品已在购物车
            ItemCartTermVo cartTermVo = (ItemCartTermVo) redisClient.hget("CART" + userId, itemId + "");
            if (cartTermVo==null){
                redisClient.hset("CART"+userId,itemId+"",getCartTerm(itemId));
            }else {//购物车已经又了
                cartTermVo.setNum(cartTermVo.getNum()+1);
                redisClient.hset("CART"+userId,itemId+"",cartTermVo);
            }
        }else {
            //第一次使用购物车
            redisClient.hset("CART"+userId,itemId+"",getCartTerm(itemId));
        }


    }

    /**
     * 获取某个人的购物车信息
     * @param userId
     * @return
     */
    @Override
    public List<ItemCartTermVo> getCart(Long userId) {
        return redisClient.hgetAll("CART"+userId);
    }

    @Override
    public ItemCartTermVo updateItemnums(Integer num, Long userId, Long itemId) {
        ItemCartTermVo cart = (ItemCartTermVo) redisClient.hget("CART" + userId, itemId + "");
        cart.setNum(num);
        redisClient.hset("CART" + userId, itemId + "",cart);
        return cart;

    }

    @Override
    public void deleteItemFromCarts(Long userId, Long itemId) {
        ItemCartTermVo cart = (ItemCartTermVo) redisClient.hget("CART" + userId, itemId +"");

        redisClient.hdel("CART"+userId,itemId+"");

    }


    /**
     * 根据商品ID  获取购物车项
     * @param itemId
     * @return
     */

    public ItemCartTermVo getCartTerm(Long itemId){
        //新增商品到购物车
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        ItemCartTermVo cartTermVo = new ItemCartTermVo();
        cartTermVo.setId(itemId);
        cartTermVo.setImage(tbItem.getImage());
        cartTermVo.setNum(1);
        cartTermVo.setPrice(tbItem.getPrice());
        cartTermVo.setTitle(tbItem.getTitle());
        cartTermVo.setSellPoint(tbItem.getSellPoint());
        return cartTermVo;
    }
}
