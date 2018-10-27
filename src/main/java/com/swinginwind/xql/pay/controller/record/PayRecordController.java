package com.swinginwind.xql.pay.controller.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.swinginwind.xql.pay.entity.PayRecord;
import com.swinginwind.xql.pay.mapper.PayRecordMapper;

@Controller
@RequestMapping("/payRecord")
public class PayRecordController {

	@Autowired
	private PayRecordMapper payRecordMapper;
	
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object>  query(PayRecord payRecord) {
		payRecord.setLimit(payRecord.getOffset() + payRecord.getLimit());
		List<PayRecord> list = payRecordMapper.getAll(payRecord);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", payRecordMapper.getCount(payRecord));
		return result;
	}
	
}
