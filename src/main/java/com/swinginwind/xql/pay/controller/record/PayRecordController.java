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
import com.swinginwind.xql.pay.entity.PayRecord;
import com.swinginwind.xql.pay.entity.TMembers;
import com.swinginwind.xql.pay.mapper.PayRecordMapper;

@Controller
@RequestMapping("/payRecord")
public class PayRecordController {

	@Autowired
	private PayRecordMapper payRecordMapper;
	
	@Autowired
	private AppConfig appConfig;
	
	@RequestMapping("/query")
	@ResponseBody
	public Map<String, Object>  query(PayRecord payRecord, HttpServletRequest request) {
		payRecord.setLimit(payRecord.getOffset() + payRecord.getLimit());
		TMembers userInfo = (TMembers) request.getSession().getAttribute("userInfo");
		Map<String, Object> result = new HashMap<String, Object>();
		if(userInfo != null) {
			boolean isAdmin = appConfig.getAdminIds().contains(userInfo.getUserid());
			if(!isAdmin)
				payRecord.setUserId(userInfo.getUserid().toString());
			List<PayRecord> list = payRecordMapper.selectByUser(payRecord);
			if(list != null) {
				for(PayRecord record : list) {
					record.setAdmin(isAdmin);
				}
			}
			result.put("rows", list);
			result.put("total", payRecordMapper.selectCountByUser(payRecord));
			
		}
		return result;
	}
	
}
