package com.usian;

import com.usian.utils.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.usian.mapper")
public class SSOServiceApp {
    @Autowired
    private RsaUtils rsaUtils;
    public static void main(String[] args) {
        SpringApplication.run(SSOServiceApp.class, args);
    }

    @PostConstruct
    public void afterCon(){
        try {
            rsaUtils.generateKey( "usian");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}