package com.swinginwind.xql.pay.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import com.jpay.unionpay.SDKConfig;

@Order(1)
public class StartupRunner implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);
	@Override
	public void run(String... args) throws Exception {
		 logger.info("startup runner");
		 //银联加载配置
		 SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
	}

}
