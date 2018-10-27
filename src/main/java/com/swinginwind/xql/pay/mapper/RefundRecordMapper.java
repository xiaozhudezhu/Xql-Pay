package com.swinginwind.xql.pay.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.swinginwind.xql.pay.entity.RefundRecord;

@Mapper
public interface RefundRecordMapper {
	
	@Insert("INSERT INTO refund_record(pay_record_id,refund_amount,refund_reason,refund_status,refund_time,record_time) VALUES(#{payRecordId}, #{refundAmount}, #{refundReason}, #{refundStatus}, #{refundTime}, #{recordTime})")
    void insert(RefundRecord refundRecord);
	
	@Select("SELECT t1.*,t2.id as pay_record_id,t2.product_name,t2.pay_way,t2.user_name,t2.total_amount,t2.pay_time FROM refund_record t1, pay_record t2 where t1.pay_record_id=t2.id order by ${sort} ${order},refund_time desc, id desc limit #{offset}, #{limit}")
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
        @Result(property = "refundAmount", column = "refund_amount"),
        @Result(property = "refundReason", column = "refund_reason"),
        @Result(property = "refundStatus", column = "refund_status"),
        @Result(property = "refundTime", column = "refund_time"),
        @Result(property = "recordTime", column = "record_time")
    })
    List<RefundRecord> getAll(RefundRecord record);
	
	@Select("SELECT count(1) FROM refund_record")
	int getCount(RefundRecord record);

}
