package com.swinginwind.xql.pay.controller.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.swinginwind.xql.pay.config.AppConfig;
import com.swinginwind.xql.pay.entity.RefundRecord;
import com.swinginwind.xql.pay.entity.TMembers;
import com.swinginwind.xql.pay.mapper.RefundRecordMapper;

@Controller
@RequestMapping("/refundRecord")
public class RefundRecordController {

	@Autowired
	private RefundRecordMapper refundRecordMapper;
	@Autowired
	private AppConfig appConfig;
	
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object>  query(RefundRecord refundRecord, HttpServletRequest request) {
		refundRecord.setLimit(refundRecord.getOffset() + refundRecord.getLimit());
		TMembers userInfo = (TMembers) request.getSession().getAttribute("userInfo");
		Map<String, Object> result = new HashMap<String, Object>();
		if(userInfo != null) {
			boolean isAdmin = appConfig.getAdminIds().contains(userInfo.getUserid());
			if(!isAdmin)
				refundRecord.setUserId(userInfo.getUserid().toString());
			List<RefundRecord> list = refundRecordMapper.selectByUser(refundRecord);
			result.put("rows", list);
			result.put("total", refundRecordMapper.selectCountByUser(refundRecord));
		}
		return result;
	}
	
}
