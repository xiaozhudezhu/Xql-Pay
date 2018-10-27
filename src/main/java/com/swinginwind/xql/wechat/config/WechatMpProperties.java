package com.swinginwind.xql.wechat.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
@Component
@PropertySource("classpath:/production/wechat.properties")
@ConfigurationProperties(prefix = "wechat.mp")
public class WechatMpProperties {
    /**	
     * 设置微信公众号的appid
     */
    private String appId;

    /**
     * 设置微信公众号的app secret
     */
    private String secret;
    
    /**	
     * 设置网站应用的appid
     */
    private String appIdWeb;

    /**
     * 设置网站应用的app secret
     */
    private String secretWeb;

    /**
     * 设置微信公众号的token
     */
    private String token;

    /**
     * 设置微信公众号的EncodingAESKey
     */
    private String aesKey;

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAesKey() {
        return this.aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }

	public String getAppIdWeb() {
		return appIdWeb;
	}

	public void setAppIdWeb(String appIdWeb) {
		this.appIdWeb = appIdWeb;
	}

	public String getSecretWeb() {
		return secretWeb;
	}

	public void setSecretWeb(String secretWeb) {
		this.secretWeb = secretWeb;
	}
}
