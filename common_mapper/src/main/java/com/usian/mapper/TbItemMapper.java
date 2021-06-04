package com.usian.mapper;

import com.usian.pojo.TbItem;
import com.usian.vo.ItemESVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbItemMapper extends Mapper<TbItem> {
    public List<ItemESVo> queryAllES();

    public void updateItemNum(@Param("id") Long id ,@Param("num") Integer num);
}