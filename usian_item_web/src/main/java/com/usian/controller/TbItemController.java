package com.usian.controller;

import com.usian.feign.TbItemFeign;
import com.usian.pojo.TbItem;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.vo.ItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/backend/item")
public class TbItemController {
    @Autowired
    private TbItemFeign tbItemFeign;

    //删除商品
    @RequestMapping("/deleteItemById")
    public Result deleteItemById(Long itemId){
        try {
            tbItemFeign.deleteItemById(itemId);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }


    //修改商品
    @GetMapping("/updateTbItem")
    public Result updateTbItem(ItemVo itemVo){
        try {
            tbItemFeign.updateTbItem(itemVo);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }

    }

    //回显方法
    @RequestMapping("/preUpdateItem")
    public Result preUpdateItem(Long itemId){
        Map<String,Object> map = tbItemFeign.preUpdateItem(itemId);
        if (map.size()>0){
            return Result.ok(map);
        }
        return Result.error("查无结果");
    }

    //商品新增

    @RequestMapping("/insertTbItem")
    public Result insertTbItem(ItemVo itemVo){
        tbItemFeign.insertTbItem(itemVo);
        return Result.ok();
    }

    //查询所有商品
//    @HystrixCommand(fallbackMethod = "selectTbItemAllByPageFallBack",
//            commandProperties = {
//                    //错误百分比条件，达到熔断器最小请求数后错误率达到百分之多少后打开熔断器
//                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE, value = "50"),
//                    //断容器最小请求数，达到这个值过后才开始计算是否打开熔断器
//                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD, value = "2"),
//                    // 默认5秒; 熔断器打开后多少秒后 熔断状态变成半熔断状态(对该微服务进行一次请求尝试，不成功则状态改成熔断，成功则关闭熔断器
//                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS, value = "5000")
//            })
    @RequestMapping("/selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam(name = "rows",defaultValue = "2")Integer rows){
       PageResult pageResult = tbItemFeign.selectTbItemAllByPage(page,rows);
       return Result.ok(pageResult);
    }

//    public Result selectTbItemAllByPageFallBack( Integer page,  Integer rows){
//        return Result.error("熔断了  降级了");
//    }



    //根据id查询
    @GetMapping("/selectItemInfo")
    public Result selectItemInfo(@RequestParam("id") Long id) {
        TbItem tbItem = tbItemFeign.selectItemInfo(id);
        if (tbItem != null) {
            return Result.ok(tbItem);
        }
        return Result.error("查无结果");
    }
}
