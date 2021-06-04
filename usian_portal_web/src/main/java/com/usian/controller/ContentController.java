package com.usian.controller;

import com.usian.feign.ContentServiceFeign;
import com.usian.utils.Result;
import com.usian.vo.BigADContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/content")
public class ContentController {
    @Autowired
    private ContentServiceFeign contentServiceFeign;

    //查询首页打广告信息
    @RequestMapping("/selectFrontendContentByAD")
    public Result selectFrontendContentByAD(){
        try {
            List<BigADContentVo> ads = contentServiceFeign.selectFrontendContentByAD();
            return Result.ok(ads);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }

    }
}
