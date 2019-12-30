package com.swinginwind.xql.pay.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swinginwind.xql.pay.entity.BaseOrder;
import com.swinginwind.xql.pay.entity.PayRecord;
import com.swinginwind.xql.pay.entity.TMembers;
import com.swinginwind.xql.pay.mapper.BaseOrderMapper;
import com.swinginwind.xql.pay.mapper.PayRecordMapper;
import com.swinginwind.xql.pay.service.OrderService;
import com.swinginwind.xql.pay.service.UserService;

@Service
public class OrderServiceImpl implements OrderService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BaseOrderMapper orderMapper;

	@Autowired
	private PayRecordMapper payRecordMapper;
	
	@Autowired
	private UserService userService;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return orderMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(BaseOrder record) {
		return orderMapper.insert(record);
	}

	@Override
	public int insertSelective(BaseOrder record) {
		return orderMapper.insertSelective(record);
	}

	@Override
	public BaseOrder selectByPrimaryKey(Integer id) {
		return orderMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(BaseOrder record) {
		return orderMapper.updateByPrimaryKey(record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(BaseOrder record) {
		return orderMapper.updateByPrimaryKeyWithBLOBs(record);
	}

	@Override
	public int updateByPrimaryKey(BaseOrder record) {
		return orderMapper.updateByPrimaryKey(record);
	}

	@Override
	public boolean wxPayNotify(Map<String, String> params) {
		//String appid = params.get("appid");
		// 商户号
		String mch_id = params.get("mch_id");
		String result_code = params.get("result_code");
		String openId = params.get("openid");
		// 交易类型
		//String trade_type = params.get("trade_type");
		// 付款银行
		//String bank_type = params.get("bank_type");
		// 总金额
		String total_fee = params.get("total_fee");
		// 现金支付金额
		//String cash_fee = params.get("cash_fee");
		// 微信支付订单号
		String transaction_id = params.get("transaction_id");
		// 商户订单号
		String orderId = params.get("out_trade_no");
		// 支付完成时间，格式为yyyyMMddHHmmss
		String time_end = params.get("time_end");
		int count = payRecordMapper.getCountByOutTradeNo(orderId);
		if (count == 0) {
			BaseOrder order = orderMapper.selectByPrimaryKey(Integer.parseInt(orderId));
			if (order == null)
				log.error("无效的订单" + orderId);
			else {
				PayRecord record = new PayRecord();
				record.setBuyerId(openId);
				record.setBuyerName(params.get("buyer_logon_id"));
				record.setBuyerPayAmount(new BigDecimal(total_fee));
				record.setOutTradeNo(orderId);
				record.setPayStatus(result_code);
				try {
					record.setPayTime(DateUtils.parseDate(time_end, "yyyyMMddHHmmss"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (order.getOrderUser() != null) {
					TMembers member = userService.selectByUserId(Integer.parseInt(order.getOrderUser()));
					if (member != null) {
						record.setUserId(member.getUserid().toString());
						record.setUserName(member.getName());
					}
				}
				record.setProductId(order.getId());
				record.setProductName("按主题购买");
				record.setPayWay("wxpay");

				record.setReceiptAmount(new BigDecimal(total_fee));
				record.setRecordTime(new Date());
				record.setSellerId(mch_id);
				record.setSellerName(mch_id);
				record.setTotalAmount(new BigDecimal(total_fee).divide(new BigDecimal(100)));
				record.setTradeNo(transaction_id);
				// record.setUserId(openId);
				record.setOutTradeNo(orderId);
				payRecordMapper.insert(record);
				BaseOrder order1 = new BaseOrder();
				order1.setId(record.getProductId());
				order1.setPayTime(new Date());
				order1.setPayUser(record.getBuyerId());
				order1.setStatus((byte) 1);
				orderMapper.updateByPrimaryKeySelective(order1);
				return true;
			}
		} else
			log.info("重复接收到支付信息");
		return false;
	}

}
