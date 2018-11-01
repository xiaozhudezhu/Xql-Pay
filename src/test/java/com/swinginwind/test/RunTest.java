package com.swinginwind.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.swinginwind.xql.pay.entity.LoginRecord;
import com.swinginwind.xql.pay.mapper.LoginRecordMapper;
import com.swinginwind.xql.pay.mapper.TMembersMapper;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RunTest {
	
	@Autowired
    private LoginRecordMapper loginRecordMapper;
	
	@Autowired
    private TMembersMapper tMembersMapper;

    @Test
    public void test() throws Exception {
    	WxMpUser user = new WxMpUser();
    	user.setOpenId("11111");
    	user.setCity("上海");
    	user.setCountry("中国");
    	user.setHeadImgUrl("111");
    	user.setLanguage("zh_CN");
    	user.setNickname("邓");
    	user.setProvince("上海");
    	user.setSex("male");
    	LoginRecord record = new LoginRecord(user);
        loginRecordMapper.insert(record);
    }

}
