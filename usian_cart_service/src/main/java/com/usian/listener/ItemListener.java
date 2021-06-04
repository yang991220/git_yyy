package com.usian.listener;

import com.usian.util.RedisClient;
import com.usian.utils.JsonUtils;
import com.usian.vo.ItemCartTermVo;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ItemListener {
    @Autowired
    private RedisClient redisClient;



    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="item_queue",durable = "true"),
            exchange = @Exchange(value="sso_exchange",type= ExchangeTypes.TOPIC),
            key= {"sso.insert"}
    ))
    public void syns(HashMap<Long,String> data){
        Set<Long> set = data.keySet();
        //使用for增强来取出key和value
        for (Long item : set) {
            String cartJson = data.get(item);
            Map<Long, ItemCartTermVo> cart = JsonUtils.jsonToMap(cartJson,Long.class, ItemCartTermVo.class);
            for (Map.Entry<Long, ItemCartTermVo> entry : cart.entrySet()) {
                Long key = entry.getKey();
                ItemCartTermVo value = entry.getValue();
                ItemCartTermVo flag = (ItemCartTermVo) redisClient.hget("CART" + item, key + "");
                if (flag == null){
                    redisClient.hset("CART"+item,key+"",value);
                }else {
                    flag.setNum(flag.getNum()+1);
                    redisClient.hset("CART"+item,key+"",flag);
                }
            }
        }

        }
    }





