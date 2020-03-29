package com.swinginwind.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.swinginwind.xql.pay.entity.LoginRecord;
import com.swinginwind.xql.pay.mapper.LoginRecordMapper;
import com.swinginwind.xql.pay.mapper.TMembersMapper;
import com.swinginwind.xql.pay.service.OrderService;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RunTest {
	
	@Autowired
    private LoginRecordMapper loginRecordMapper;
	
	@Autowired
    private TMembersMapper tMembersMapper;
	
	@Autowired
    private OrderService orderService;

    @Test
    public void test() throws Exception {
    	/*WxMpUser user = new WxMpUser();
    	user.setOpenId("11111");
    	user.setCity("上海");
    	user.setCountry("中国");
    	user.setHeadImgUrl("111");
    	user.setLanguage("zh_CN");
    	user.setNickname("邓");
    	user.setProvince("上海");
    	user.setSex("male");
    	LoginRecord record = new LoginRecord(user);
        loginRecordMapper.insert(record);*/
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("mch_id", "111");
    	params.put("result_code", "200");
    	params.put("openid", "111");
    	params.put("total_fee", "123");
    	params.put("transaction_id", "transaction_id");
    	params.put("out_trade_no", "13");
    	params.put("time_end", "20200329174100");
    	orderService.wxPayNotify(params);
    }

}
