package com.usian.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.usian.api.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

//@Component
public class LoginFilter extends ZuulFilter {
    @Autowired
    private UserFeign userFeign;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {





        RequestContext rc = RequestContext.getCurrentContext();
        HttpServletRequest request = rc.getRequest();
        //如果是订单微服务，就执行拦截代码 如果不是就不敢接不处理
        System.out.println(request.getRequestURI());
        boolean b = request.getRequestURI().startsWith("/item");

        if (!b){
            return null;
        }

        String token = request.getHeader("token");

        final String[] cookieToken = new String[1];
        if (token ==null || token.equals("")){
            token = request.getParameter("token");
            if (token ==null || token.equals("")){
                Cookie[] cookies = request.getCookies();


                if (cookies!=null){
                    Arrays.asList(cookies).forEach(cookie -> {
                        if (cookie.getName().equals("token")){
                            cookieToken[0] =cookie.getValue();
                        }
                    });
                    token =cookieToken[0];
                }


            }
        }
        //用户没有携带token

        if (token==null || token.equals("")){
            rc.setSendZuulResponse(false);// 代表请求结束。不在继续向下请求
            rc.setResponseStatusCode(401);// 添加一个响应的状态码
            return null;
        }

        //用户携带token，验证有效
        boolean userByToken = userFeign.getUserByToken(token);
        if (!userByToken){//拦截
            rc.setSendZuulResponse(false);// 代表请求结束。不在继续向下请求
            rc.setResponseStatusCode(401);// 添加一个响应的状态码
//            rc.setResponseBody("请登录后再访问！！！");// 响应内容
//            rc.getResponse().setContentType("text/html;charset=utf-8");// 响应类型
        }








        return null;
    }
}
