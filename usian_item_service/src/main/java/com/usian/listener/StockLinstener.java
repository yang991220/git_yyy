package com.usian.listener;

import com.usian.mapper.TbItemMapper;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StockLinstener {

    @Autowired
    private TbItemMapper itemMapper;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="stock_queue",durable = "true"),
            exchange = @Exchange(value="deduction_stock_exchange",type= ExchangeTypes.TOPIC),
            key= {"deduction.stock"}
    ))
    public void listen(Map<String,Integer> itemNumMap){
        //减库存

        itemNumMap.forEach((id,num)->{

            itemMapper.updateItemNum(Long.parseLong(id),num);
        });











    }
}
