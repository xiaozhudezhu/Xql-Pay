package com.swinginwind.xql.pay.controller.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.swinginwind.xql.pay.entity.PayRecord;
import com.swinginwind.xql.pay.entity.RefundRecord;
import com.swinginwind.xql.pay.mapper.PayRecordMapper;
import com.swinginwind.xql.pay.mapper.RefundRecordMapper;

@Controller
@RequestMapping("/refundRecord")
public class RefundRecordController {

	@Autowired
	private RefundRecordMapper refundRecordMapper;
	
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object>  query(RefundRecord refundRecord) {
		refundRecord.setLimit(refundRecord.getOffset() + refundRecord.getLimit());
		List<RefundRecord> list = refundRecordMapper.getAll(refundRecord);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", refundRecordMapper.getCount(refundRecord));
		return result;
	}
	
}
