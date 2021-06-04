package com.usian.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CloseOverTimeOrder {

    @Scheduled(cron="0/0 1 * * * ?")
    public void task(){

        //关闭超时订单
        //1.去读数据库 未支付的 超时的订单

        //2.关闭这些符合条件的订单
    }
}

