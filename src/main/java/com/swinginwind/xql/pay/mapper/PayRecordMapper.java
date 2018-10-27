package com.swinginwind.xql.pay.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.swinginwind.xql.pay.entity.PayRecord;

@Mapper
public interface PayRecordMapper {
	
	@Insert("INSERT INTO pay_record(product_id,product_name,pay_way,trade_no,out_trade_no,user_id,user_name,buyer_id,buyer_name,seller_id,seller_name,total_amount,receipt_amount,buyer_pay_amount,pay_status,pay_time,record_time) VALUES(#{productId}, #{productName}, #{payWay}, #{tradeNo}, #{outTradeNo}, #{userId}, #{userName}, #{buyerId}, #{buyerName}, #{sellerId}, #{sellerName}, #{totalAmount}, #{receiptAmount}, #{buyerPayAmount}, #{payStatus}, #{payTime}, #{recordTime})")
    void insert(PayRecord payRecord);
	
	@Select("SELECT * FROM pay_record order by ${sort} ${order},pay_time desc, id desc limit #{offset}, #{limit}")
    @Results({
        @Result(property = "id",  column = "id"),
        @Result(property = "productId", column = "product_id"),
        @Result(property = "productName", column = "product_name"),
        @Result(property = "payWay", column = "pay_way"),
        @Result(property = "tradeNo", column = "trade_no"),
        @Result(property = "outTradeNo", column = "out_trade_no"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "userName", column = "user_name"),
        @Result(property = "buyerId", column = "buyer_id"),
        @Result(property = "buyerName", column = "buyer_name"),
        @Result(property = "sellerId", column = "seller_id"),
        @Result(property = "sellerName", column = "seller_name"),
        @Result(property = "totalAmount", column = "total_amount"),
        @Result(property = "receiptAmount", column = "receipt_amount"),
        @Result(property = "buyerPayAmount", column = "buyer_pay_amount"),
        @Result(property = "payTime", column = "pay_time"),
        @Result(property = "recordTime", column = "record_time")
    })
    List<PayRecord> getAll(PayRecord record);
	
	@Select("SELECT count(1) FROM pay_record")
	int getCount(PayRecord record);
	
	@Select("SELECT count(1) FROM pay_record where out_trade_no=#{outTradeNo}")
	int getCountByOutTradeNo(String outTradeNo);

}
