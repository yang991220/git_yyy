package com.usian.filter;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 降级类：设置zuul无法调用consumer时的降级逻辑
 */
@Component
public class UserProviderFallBack implements FallbackProvider {
    /**
     * 指定要降级的服务
     * @return
     */
    @Override
    public String getRoute() {
        return "*";//*：所有服务
    }

    /**
     *
     * @param route
     * @param cause
     * @return
     */
    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse(){

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                return httpHeaders;
            }

            /**
             * 返回托底数据
             * @return
             * @throws IOException
             */
            @Override
            public InputStream getBody() throws IOException {
                String msg = route+" 服务不可用，请联系管理员，联系电话：110";
                return new ByteArrayInputStream(msg.getBytes());
            }

            /**
             * 返回相应的状态码：
             *  zuul转请求失败了，但是客户端向zuul发送请求是成功的，所以不应该把404、500等问题抛给客
             *  户端
             * @return
             * @throws IOException
             */
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return this.getStatusCode().value();
            }

            @Override
            public String getStatusText() throws IOException {
                return this.getStatusCode().getReasonPhrase();
            }

            @Override
            public void close() {

            }
        };
    }
}