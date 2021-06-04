package com.usian.listener;

import com.rabbitmq.client.Channel;
import com.usian.dto.ItemDto;
import com.usian.util.ESUtil;
import com.usian.utils.JsonUtils;
import com.usian.vo.ItemESVo;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ItemListener {

    @Autowired
    private ESUtil esUtil;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="item_queue",durable = "true"),
            exchange = @Exchange(value="item_exchange",type= ExchangeTypes.TOPIC),
            key= {"item.insert"}
    ))
    public void insertItem(ItemDto itemDto, Channel channel, Message message){
        ItemESVo itemESVo = new ItemESVo();
        itemESVo.setId(itemDto.getTbItem().getId());
        itemESVo.setItemTitle(itemDto.getTbItem().getTitle());
        itemESVo.setItemSellPoint(itemDto.getTbItem().getSellPoint());
        itemESVo.setItemPrice(itemDto.getTbItem().getPrice()+"");
        itemESVo.setItemImage(itemDto.getTbItem().getImage());
        itemESVo.setItemDesc(itemDto.getTbItemDesc().getItemDesc());
        itemESVo.setItemCategoryName(itemDto.getCategoryName());

        String sourse = JsonUtils.objectToJson(itemESVo);

        //新增消息到es
        esUtil.insertDoc("usian","item",sourse);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
        }
    }




}
