package com.swinginwind.xql.pay.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/production/app.properties")
@ConfigurationProperties
public class AppConfig {
	
	private List<Integer> adminIds;
	
	private String xqlUrl;
	
	private String fileDir;

	public List<Integer> getAdminIds() {
		return adminIds;
	}

	public void setAdminIds(List<Integer> adminIds) {
		this.adminIds = adminIds;
	}

	public String getXqlUrl() {
		return xqlUrl;
	}

	public void setXqlUrl(String xqlUrl) {
		this.xqlUrl = xqlUrl;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}


}
