package com.usian.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

//@Component
public class RateLimitFilter extends ZuulFilter {

    RateLimiter rateLimiter = RateLimiter.create(1);


    @Override
    public String filterType() {

        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext rc = RequestContext.getCurrentContext();
        if (!rateLimiter.tryAcquire()){
            rc.setSendZuulResponse(false);// 代表请求结束。不在继续向下请求
            rc.setResponseStatusCode(402);// 添加一个响应的状态码
            throw new ZuulException("限流啦",403,"限流啦");
        }


        return null;
    }
}
