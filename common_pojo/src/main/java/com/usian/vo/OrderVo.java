package com.usian.vo;

import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderShipping;

public class OrderVo {
    private String orderItem;   //订单项json数组
    private TbOrder order;
    private TbOrderShipping orderShipping;

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public TbOrder getOrder() {
        return order;
    }

    public void setOrder(TbOrder order) {
        this.order = order;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }

    public OrderVo() {
    }

    public OrderVo(String orderItem, TbOrder order, TbOrderShipping orderShipping) {
        this.orderItem = orderItem;
        this.order = order;
        this.orderShipping = orderShipping;
    }
}
