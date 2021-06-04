package com.usian.controller;

import com.usian.api.CartFeign;
import com.usian.feign.TbItemFeign;
import com.usian.pojo.TbItem;
import com.usian.utils.CookieUtils;
import com.usian.utils.JsonUtils;
import com.usian.utils.Result;
import com.usian.vo.ItemCartTermVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/frontend/cart")
public class CartController {


    @Autowired
    private TbItemFeign itemFeign;//查询商品微服务的一个feign
    @Autowired
    private CartFeign cartFeign;

    /**
     *删除购物车项
     */
    @RequestMapping("/deleteItemFromCart")
    public Result deleteItemFromCart(@RequestParam("userId") Long userId,@RequestParam("itemId") Long itemId
            ,HttpServletRequest request,HttpServletResponse response){
        if (userId==null){//没有登录
            String cartJson = CookieUtils.getCookieValue(request, "CART", true);
            Map<Long,ItemCartTermVo> cart = JsonUtils.jsonToMap(cartJson, Long.class,ItemCartTermVo.class);
            cart.remove(itemId);
            CookieUtils.setCookie(request,response,"CART",JsonUtils.objectToJson(cart),true);
        }else {//没有登录
            cartFeign.deleteItemFromCarts(userId,itemId);
        }
        return Result.ok();
    }

    /**
     * 修改购物车
     *
     */
    @RequestMapping("/updateItemNum")
    public Result updateItemNum(@RequestParam("num") Integer num,@RequestParam("userId") Long userId
            ,@RequestParam("itemId") Long itemId,HttpServletRequest request,HttpServletResponse response){
        //判断是否登录
        if (userId==null){//未登录
            String cartJson = CookieUtils.getCookieValue(request, "CART", true);
            Map<Long,ItemCartTermVo> cart = JsonUtils.jsonToMap(cartJson, Long.class,ItemCartTermVo.class);
            ItemCartTermVo cartTermVo = cart.get(itemId);
            cartTermVo.setNum(num);
            CookieUtils.setCookie(request,response,"CART",JsonUtils.objectToJson(cart),true);

        }else {//已登录
            cartFeign.updateItemnums(num,userId,itemId);
        }
        return Result.ok();
    }

    /**
     * 购物车展示接口
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping("/showCart")
    public Result showCart(@RequestParam("userId") Long userId,HttpServletRequest request){
        if (userId==null){
            String cartJson = CookieUtils.getCookieValue(request, "CART", true);
            Map<Long,ItemCartTermVo> CartMap = JsonUtils.jsonToMap(cartJson, Long.class,ItemCartTermVo.class);
            Collection<ItemCartTermVo> values = CartMap.values();
            return Result.ok(values);

        }else {//登录
            Collection<ItemCartTermVo> cartTermVos= cartFeign.getCart(userId);
            return Result.ok(cartTermVos);
        }

    }


    /**
     * 添加商品到购物车
     *
     * @param userId
     * @param itemId
     * @return request 获取当前浏览器已有的cookie
     * response  可以更改浏览器中cokkie的值
     */

    @RequestMapping("/addItem")
    public Result addItem(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId, HttpServletRequest request, HttpServletResponse response) {

        /*
                1.判断是否为登录过的




         */

        if (userId == null) {//  没登录： cookie  web服务
            //往浏览器的cookie去存储值
            //判断用户是否是第一次使用购物车
            String cartJson = CookieUtils.getCookieValue(request, "CART", true);
            if (cartJson== null || cartJson.equals("")){
                //如果是第一次使用购物车 创建一个cookie  往cookie增加购物车项
                HashMap<Long, ItemCartTermVo> cartMap = new HashMap<>();

                //新增商品到购物车
               addCartTerm(itemId,cartMap);

                CookieUtils.setCookie(request,response,"CART", JsonUtils.objectToJson(cartMap),true);
            }else {
                //如果不是第一次 获取上一次购物车cookie的值 往里边追加新的内容即可
                Map<Long,ItemCartTermVo> cart = JsonUtils.jsonToMap(cartJson, Long.class,ItemCartTermVo.class);


                //判断该购买的商品是否已经加入过购物车
                ItemCartTermVo cartTermVo = cart.get(itemId);
                if (cartTermVo==null){
                    //没有加入过，新增一个购物车项
                    //新增商品到购物车
                    addCartTerm(itemId,cart);
                }else {
                    //如果加入过，需要修该他的数量（原有数量+1）
                    cartTermVo.setNum(cartTermVo.getNum()+1);
                }
                //将修改后的购物车  更新到cookie
                CookieUtils.setCookie(request,response,"CART",JsonUtils.objectToJson(cart),true);
            }








        } else {// 登录 redis  service服务

            cartFeign.addItem(userId,itemId);


        }
        return Result.ok();
    }

    /**
     * 新增购物车项
     * @param itemId
     * @param cartMap
     */
    public void addCartTerm(Long itemId,Map cartMap){
        //新增商品到购物车
        TbItem tbItem = itemFeign.selectItemInfo(itemId);
        ItemCartTermVo cartTermVo = new ItemCartTermVo();
        cartTermVo.setId(itemId);
        cartTermVo.setImage(tbItem.getImage());
        cartTermVo.setNum(1);
        cartTermVo.setPrice(tbItem.getPrice());
        cartTermVo.setTitle(tbItem.getTitle());
        cartTermVo.setSellPoint(tbItem.getSellPoint());
        cartMap.put(itemId,cartTermVo);
    }
}
