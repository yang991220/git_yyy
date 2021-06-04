package com.usian.service.impl;

import com.usian.mapper.TbOrderItemMapper;
import com.usian.mapper.TbOrderMapper;
import com.usian.mapper.TbOrderShippingMapper;
import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderItem;
import com.usian.pojo.TbOrderShipping;
import com.usian.service.OrderService;
import com.usian.util.RedisClient;
import com.usian.utils.JsonUtils;
import com.usian.vo.ItemCartTermVo;
import com.usian.vo.OrderVo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;
    /**
     * 从购物车项目中  选择用户选择的购买的购物车项
     *
     * @param ids
     * @param userId
     * @return
     */
    @Override
    public List<ItemCartTermVo> selectOrderTerms(Long[] ids, Long userId) {
        //1. 从redis中 获取当前用户， 所有的购物车项的数据
        List<ItemCartTermVo> cartTermVos = redisClient.hgetAll("CART" + userId);


        //2.筛选出用户选择的购物车项的信息

        //方式二：1.8新特性
        // s 集合中的某一个元素
        List<ItemCartTermVo> orderTrems = cartTermVos.stream().filter(cartTerm -> {
            //判断该购物车项 是否是用户选择的

            long count = Arrays.stream(ids).filter(id -> {
                if (id.equals(cartTerm.getId())) {
                    return true;
                }
                return false;
            }).count();

            if (count==1){
                return true;
            }


            return false;
        }).collect(Collectors.toList());


        //方式一  复杂的写法
        //2.筛选出用户选择的购物车项的信息
//        ArrayList<ItemCartTermVo> orderTrems = new ArrayList<>();  //选择购买的购物车项的集合  订单项集合
//        for (Long id : ids) {
//            for (ItemCartTermVo cartTermVo : cartTermVos) {
//                if (cartTermVo.getId().equals(id)) {
//                    orderTrems.add(cartTermVo);
//                    break;      //break 结束当前循环  continue 结束本次循环 执行下一次循环
//                }
//            }
//        }
        return orderTrems;

    }

    @Transactional
    @Override
    public String insertOrder(OrderVo orderVo) {
        //订单基本表 1条
        TbOrder order = orderVo.getOrder();
        //生成一个订单Id  一定要保证唯一性
        //用户id+当前时间+随机数
        Date date = new Date();
        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        order.setStatus(1);
        order.setCreateTime(date);
        orderMapper.insertSelective(order);

        //订单项 多条
        String orderItem = orderVo.getOrderItem();
        List<TbOrderItem> tbOrderItems = JsonUtils.jsonToList2(orderItem, TbOrderItem.class);
        tbOrderItems.forEach(orderItemss ->{
            orderItemss.setId(UUID.randomUUID().toString());
            orderItemss.setOrderId(orderId);
            orderItemMapper.insertSelective(orderItemss);
        });


        //订单收货地址 1条
        TbOrderShipping orderShipping = orderVo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(date);
        orderShippingMapper.insertSelective(orderShipping);

        //修改库存
        /**
         * 根据 订单项购买的商品的数量，去减少 商品表中的对应商品的库存量
         *
         *
         */


        Map<String, Integer> itemNumMap = tbOrderItems.stream().collect(Collectors.toMap(TbOrderItem::getItemId,TbOrderItem::getNum));

        amqpTemplate.convertAndSend("deduction_stock_exchange","deduction.stock",itemNumMap);



        return orderId;
    }
}
