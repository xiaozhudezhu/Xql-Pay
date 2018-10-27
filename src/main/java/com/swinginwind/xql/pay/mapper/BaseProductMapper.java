package com.swinginwind.xql.pay.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.swinginwind.xql.pay.entity.BaseProduct;

@Mapper
public interface BaseProductMapper {
	
	@Select("SELECT * FROM base_product where enabled='1' order by sort_code, id")
    @Results({
        @Result(property = "id",  column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "amount", column = "amount"),
        @Result(property = "amountUnit", column = "amount_unit"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "durationUnit", column = "duration_unit")
    })
    List<BaseProduct> getAll();
	
	@Select("SELECT * FROM base_product where id = #{id}")
    @Results({
        @Result(property = "id",  column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "amount", column = "amount"),
        @Result(property = "amountUnit", column = "amount_unit"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "durationUnit", column = "duration_unit")
    })
    List<BaseProduct> getById(int id);

}
