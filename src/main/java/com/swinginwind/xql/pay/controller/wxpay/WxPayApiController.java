package com.swinginwind.xql.pay.controller.wxpay;

import com.jpay.weixin.api.WxPayApiConfig;

public abstract class WxPayApiController{
	public abstract WxPayApiConfig getApiConfig();
}