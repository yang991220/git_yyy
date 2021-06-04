package com.usian.fegin;

import com.usian.vo.ItemESVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "usian-search-service")
public interface SearchFegin {



    @RequestMapping("/search/list")
    public List<ItemESVo> search(@RequestParam("q") String q);
}
