package com.swinginwind.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.swinginwind.core.sms.SDKSendTemplateSMS;

@Configuration
public class SmsConfig {
	
	@Value("${sms.sid}")
	private String sid;
	@Value("${sms.token}")
	private String token;
	@Value("${sms.appid}")
	private String appid;
	
	@Bean
	public SDKSendTemplateSMS getSmsLib() {
		SDKSendTemplateSMS lib = new SDKSendTemplateSMS();
		lib.init(sid, token, appid);
		return lib;
	}
	
	

}
