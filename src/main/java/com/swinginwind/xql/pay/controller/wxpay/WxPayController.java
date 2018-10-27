package com.swinginwind.xql.pay.controller.wxpay;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jpay.ext.kit.HttpKit;
import com.jpay.ext.kit.IpKit;
import com.jpay.ext.kit.PaymentKit;
import com.jpay.ext.kit.StrKit;
import com.jpay.ext.kit.ZxingKit;
import com.jpay.vo.AjaxResult;
import com.jpay.weixin.api.WxPayApi;
import com.jpay.weixin.api.WxPayApi.TradeType;
import com.jpay.weixin.api.WxPayApiConfig;
import com.jpay.weixin.api.WxPayApiConfig.PayModel;
import com.swinginwind.xql.pay.entity.BaseProduct;
import com.swinginwind.xql.pay.entity.H5ScencInfo;
import com.swinginwind.xql.pay.entity.PayRecord;
import com.swinginwind.xql.pay.entity.RefundRecord;
import com.swinginwind.xql.pay.entity.WxPayBean;
import com.swinginwind.xql.pay.entity.H5ScencInfo.H5;
import com.swinginwind.xql.pay.mapper.BaseProductMapper;
import com.swinginwind.xql.pay.mapper.PayRecordMapper;
import com.swinginwind.xql.pay.mapper.RefundRecordMapper;
import com.swinginwind.xql.wechat.config.WechatMpProperties;
import com.jpay.weixin.api.WxPayApiConfigKit;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

@Controller
@RequestMapping("/wxpay")
public class WxPayController extends WxPayApiController {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WxPayBean wxPayBean;

	@Autowired
	WechatMpProperties wechatMpProperties;

	String notify_url;
	
	@Autowired
	private PayRecordMapper payRecordMapper;
	
	@Autowired
    private BaseProductMapper baseProductMapper;
	
	@Autowired
	private RefundRecordMapper refundRecordMapper;
	

	@Override
	public WxPayApiConfig getApiConfig() {
		notify_url = wxPayBean.getDomain().concat("/wxpay/pay_notify");
		return WxPayApiConfig.New().setAppId(wxPayBean.getAppId()).setMchId(wxPayBean.getMchId())
				.setPaternerKey(wxPayBean.getPartnerKey()).setPayModel(PayModel.BUSINESSMODEL);
	}

	@RequestMapping("")
	@ResponseBody
	public String index() {
		log.info("欢迎使用商户模式下微信支付");
		log.info(wxPayBean.toString());
		return ("欢迎使用商户模式下微信支付");
	}

	@RequestMapping("/test")
	@ResponseBody
	public String test() {
		return wxPayBean.toString();
	}

	@RequestMapping("/testWeChat")
	@ResponseBody
	public String testWeChat() {
		return wechatMpProperties.toString();
	}

	@RequestMapping("/getKey")
	@ResponseBody
	public String getKey() {
		return WxPayApi.getsignkey(wxPayBean.getAppId(), wxPayBean.getPartnerKey());
	}

	@RequestMapping("/ctp")
	@ResponseBody
	public String ctp(HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/");
		return dir;
	}

	/**
	 * 微信H5 支付 注意：必须再web页面中发起支付且域名已添加到开发配置中
	 */
	@RequestMapping(value = "/wapPay", method = { RequestMethod.POST, RequestMethod.GET })
	public void wapPay(HttpServletRequest request, HttpServletResponse response, PayRecord payRecord) {
		String ip = IpKit.getRealIp(request);
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		else {
			ip = ip.split(",")[0].trim();
		}
		H5ScencInfo sceneInfo = new H5ScencInfo();

		H5 h5_info = new H5();
		h5_info.setType("Wap");
		// 此域名必须在商户平台--"产品中心"--"开发配置"中添加

		h5_info.setWap_url(wxPayBean.getDomain());
		h5_info.setWap_name("学球乐");
		sceneInfo.setH5_info(h5_info);
		WxMpUser user = (WxMpUser) request.getSession().getAttribute("wxUser");
		String attach = "学球乐-" + payRecord.getProductName() + "###" + payRecord.getProductId() + "###" + (user != null ? user.getNickname() : "");
		String subject = "学球乐-" + payRecord.getProductName();
		String totalAmount = payRecord.getTotalAmount().toString();
		Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig().setAttach(attach)
				.setBody(subject).setSpbillCreateIp(ip).setTotalFee(totalAmount)
				.setTradeType(TradeType.MWEB).setNotifyUrl(notify_url)
				.setOutTradeNo(String.valueOf(System.currentTimeMillis())).setSceneInfo(h5_info.toString()).build();

		String xmlResult = WxPayApi.pushOrder(false, params);
		log.info(xmlResult);
		Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

		String return_code = result.get("return_code");
		String return_msg = result.get("return_msg");
		if (!PaymentKit.codeIsOK(return_code)) {
			log.error("return_code>" + return_code + " return_msg>" + return_msg);
			throw new RuntimeException(return_msg);
		}
		String result_code = result.get("result_code");
		if (!PaymentKit.codeIsOK(result_code)) {
			log.error("result_code>" + result_code + " return_msg>" + return_msg);
			throw new RuntimeException(return_msg);
		}
		// 以下字段在return_code 和result_code都为SUCCESS的时候有返回

		String prepay_id = result.get("prepay_id");
		String mweb_url = result.get("mweb_url");

		log.info("prepay_id:" + prepay_id + " mweb_url:" + mweb_url);
		try {
			response.sendRedirect(mweb_url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 公众号支付
	 */
	@RequestMapping(value = "/webPay", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public AjaxResult webPay(HttpServletRequest request, HttpServletResponse response,
			PayRecord payRecord) {
		AjaxResult result = new AjaxResult();
		// openId，采用 网页授权获取 access_token API：SnsAccessTokenApi获取
		String openId = (String) request.getSession().getAttribute("openId");

		if (StrKit.isBlank(openId)) {
			result.addError("openId is null");
			return result;
		}

		String ip = IpKit.getRealIp(request);
		System.out.println(ip);
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		ip = "127.0.0.1";
		WxMpUser user = (WxMpUser) request.getSession().getAttribute("wxUser");
		String attach = "学球乐-" + payRecord.getProductName() + "###" + payRecord.getProductId() + "###" + (user != null ? user.getNickname() : "");
		String subject = "学球乐-" + payRecord.getProductName();
		String totalAmount = payRecord.getTotalAmount().toString();
		Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig().setAttach(attach)
				.setBody(subject).setOpenId(openId).setSpbillCreateIp(ip).setTotalFee(totalAmount)
				.setTradeType(TradeType.JSAPI).setNotifyUrl(notify_url)
				.setOutTradeNo(String.valueOf(System.currentTimeMillis())).build();

		String xmlResult = WxPayApi.pushOrder(false, params);
		log.info(xmlResult);
		Map<String, String> resultMap = PaymentKit.xmlToMap(xmlResult);

		String return_code = resultMap.get("return_code");
		String return_msg = resultMap.get("return_msg");
		if (!PaymentKit.codeIsOK(return_code)) {
			result.addError(return_msg);
			return result;
		}
		String result_code = resultMap.get("result_code");
		if (!PaymentKit.codeIsOK(result_code)) {
			result.addError(return_msg);
			return result;
		}
		// 以下字段在return_code 和result_code都为SUCCESS的时候有返回

		String prepay_id = resultMap.get("prepay_id");

		Map<String, String> packageParams = PaymentKit.prepayIdCreateSign(prepay_id);

		String jsonStr = JSON.toJSONString(packageParams);
		result.success(jsonStr);
		return result;
	}

	/**
	 * 生成支付二维码（模式一）并在页面上显示
	 */
	@RequestMapping(value = "/scanCode1", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public AjaxResult scanCode1(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("productId") String product_id) {
		AjaxResult result = new AjaxResult();
		try {
			if (StrKit.isBlank(product_id)) {
				result.addError("productId is null");
				return result;
			}
			WxPayApiConfig config = WxPayApiConfigKit.getWxPayApiConfig();
			// 获取扫码支付（模式一）url

			String qrCodeUrl = WxPayApi.getCodeUrl(config.getAppId(), config.getMchId(), product_id,
					config.getPaternerKey(), true);
			log.info(qrCodeUrl);
			// 生成二维码保存的路径

			String name = "payQRCode1_" + product_id + ".png";
			System.out.println(request.getServletContext().getRealPath("/") + File.separator + name);
			Boolean encode = ZxingKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200,
					200, request.getServletContext().getRealPath("/") + File.separator + name);
			if (encode) {
				// 在页面上显示
				result.success(name);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.addError("系统异常：" + e.getMessage());
			return result;
		}

		return null;
	}

	/**
	 * 扫码支付模式一回调 已测试
	 */
	@RequestMapping(value = "/wxpay", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String wxpay(HttpServletRequest request, HttpServletResponse response) {
		try {

			String result = HttpKit.readData(request);
			System.out.println("callBack_xml>>>" + result);
			/**
			 * 
			 * 获取返回的信息内容中各个参数的值
			 * 
			 */
			Map<String, String> map = PaymentKit.xmlToMap(result);
			for (String key : map.keySet()) {
				System.out.println("key= " + key + " and value= " + map.get(key));
			}

			String appid = map.get("appid");
			String openid = map.get("openid");
			String mch_id = map.get("mch_id");
			String is_subscribe = map.get("is_subscribe");
			String nonce_str = map.get("nonce_str");
			String product_id = map.get("product_id");
			String sign = map.get("sign");
			Map<String, String> packageParams = new HashMap<String, String>();
			packageParams.put("appid", appid);
			packageParams.put("openid", openid);
			packageParams.put("mch_id", mch_id);
			packageParams.put("is_subscribe", is_subscribe);
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("product_id", product_id);

			String packageSign = PaymentKit.createSign(packageParams,
					WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey());
			// 统一下单文档地址：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1

			String ip = IpKit.getRealIp(request);
			if (StrKit.isBlank(ip)) {
				ip = "127.0.0.1";
			}

			Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig().setAttach("IJPay 扫码模式一测试  -By Javen")
					.setBody("IJPay 扫码模式一测试  -By Javen").setOpenId(openid).setSpbillCreateIp(ip).setTotalFee("100")
					.setTradeType(TradeType.NATIVE).setNotifyUrl(notify_url)
					.setOutTradeNo(String.valueOf(System.currentTimeMillis())).build();

			String xmlResult = WxPayApi.pushOrder(false, params);
			log.info("prepay_xml>>>" + xmlResult);

			/**
			 * 
			 * 发送信息给微信服务器
			 * 
			 */
			Map<String, String> payResult = PaymentKit.xmlToMap(xmlResult);

			String return_code = payResult.get("return_code");
			String result_code = payResult.get("result_code");

			if (StrKit.notBlank(return_code) && StrKit.notBlank(result_code) && return_code.equalsIgnoreCase("SUCCESS")
					&& result_code.equalsIgnoreCase("SUCCESS")) {
				// 以下字段在return_code 和result_code都为SUCCESS的时候有返回

				String prepay_id = payResult.get("prepay_id");

				Map<String, String> prepayParams = new HashMap<String, String>();
				prepayParams.put("return_code", "SUCCESS");
				prepayParams.put("appId", appid);
				prepayParams.put("mch_id", mch_id);
				prepayParams.put("nonceStr", System.currentTimeMillis() + "");
				prepayParams.put("prepay_id", prepay_id);
				String prepaySign = null;
				if (sign.equals(packageSign)) {
					prepayParams.put("result_code", "SUCCESS");
				} else {
					prepayParams.put("result_code", "FAIL");
					prepayParams.put("err_code_des", "订单失效"); // result_code为FAIL时，添加该键值对，value值是微信告诉客户的信息

				}
				prepaySign = PaymentKit.createSign(prepayParams,
						WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey());
				prepayParams.put("sign", prepaySign);
				String xml = PaymentKit.toXml(prepayParams);
				log.error(xml);
				return xml;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 扫码支付模式二 已测试
	 */
	@RequestMapping(value = "/scanCode2", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public AjaxResult scanCode2(HttpServletRequest request, HttpServletResponse response,
			PayRecord payRecord) {
		AjaxResult result = new AjaxResult();
		// openId，采用 网页授权获取 access_token API：SnsAccessTokenApi获取
		String openId = (String) request.getSession().getAttribute("openId");
		openId = "oCVr-wJKWVuLc8E_8rQNPgJYmCBE";
		if (StrKit.isBlank(openId)) {
			result.addError("openId is null");
			return result;
		}

		String ip = IpKit.getRealIp(request);
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		ip = "127.0.0.1";
		WxMpUser user = (WxMpUser) request.getSession().getAttribute("wxUser");
		String attach = "学球乐-" + payRecord.getProductName() + "###" + payRecord.getProductId() + "###" + "test";
		String subject = "学球乐-" + payRecord.getProductName();
		String totalAmount = payRecord.getTotalAmount().toString();
		
		Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig().setAttach(attach)
				.setBody(subject).setOpenId(openId).setSpbillCreateIp(ip).setTotalFee(totalAmount)
				.setTradeType(TradeType.NATIVE).setNotifyUrl(notify_url)
				.setOutTradeNo(String.valueOf(System.currentTimeMillis())).build();

		String xmlResult = WxPayApi.pushOrder(false, params);

		log.info(xmlResult);
		Map<String, String> resultMap = PaymentKit.xmlToMap(xmlResult);

		String return_code = resultMap.get("return_code");
		String return_msg = resultMap.get("return_msg");
		if (!PaymentKit.codeIsOK(return_code)) {
			System.out.println(xmlResult);
			result.addError("error:" + return_msg);
			return result;
		}
		String result_code = resultMap.get("result_code");
		if (!PaymentKit.codeIsOK(result_code)) {
			System.out.println(xmlResult);
			result.addError("error:" + return_msg);
			return result;
		}
		// 生成预付订单success

		String qrCodeUrl = resultMap.get("code_url");
		String name = "payQRCode2.png";

		Boolean encode = ZxingKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
				request.getServletContext().getRealPath("/") + File.separator + name);
		if (encode) {
			// renderQrCode(qrCodeUrl, 200, 200);

			// 在页面上显示

			result.success(name);
			return result;
		}
		return null;
	}

	/**
	 * 刷卡支付 已测试
	 */
	@RequestMapping(value = "/micropay", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public AjaxResult micropay(HttpServletRequest request, HttpServletResponse response) {
		AjaxResult result = new AjaxResult();
		
		String auth_code = request.getParameter("auth_code");
		String total_fee = request.getParameter("total_fee");

		if (StrKit.isBlank(total_fee)) {
			result.addError("支付金额不能为空");
			return result;
		}
		if (StrKit.isBlank(auth_code)) {
			result.addError("auth_code参数错误");
			return result;
		}

		String ip = IpKit.getRealIp(request);
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}

		Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig().setAttach("IJPay 测试  -By Javen")
				.setBody("IJPay 刷卡支付测试 -By Javen").setSpbillCreateIp(ip).setTotalFee(total_fee).setAuthCode(auth_code)
				.setTradeType(TradeType.MICROPAY).setNotifyUrl(notify_url)
				.setOutTradeNo(String.valueOf(System.currentTimeMillis())).build();

		String xmlResult = WxPayApi.micropay(false, params);

		// 同步返回结果

		log.info("xmlResult:" + xmlResult);

		Map<String, String> resultMap = PaymentKit.xmlToMap(xmlResult);
		String return_code = resultMap.get("return_code");
		String return_msg = resultMap.get("return_msg");
		if (!PaymentKit.codeIsOK(return_code)) {
			// 通讯失败

			String err_code = resultMap.get("err_code");
			if (StrKit.notBlank(err_code)) {
				// 用户支付中，需要输入密码

				if (err_code.equals("USERPAYING")) {
					// 等待5秒后调用【查询订单API】https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_2

				}
			}
			log.info("提交刷卡支付失败>>" + xmlResult);
			result.addError(return_msg);
			return result;
		}

		String result_code = resultMap.get("result_code");
		if (!PaymentKit.codeIsOK(result_code)) {
			// 支付失败
			log.info("支付失败>>" + xmlResult);
			String err_code_des = resultMap.get("err_code_des");

			result.addError(err_code_des);
			return result;
		}
		// 支付成功
		result.success(xmlResult);
		return result;
	}

	/**
	 * 微信APP支付
	 */
	@RequestMapping(value = "/appPay", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public AjaxResult appPay(HttpServletRequest request) {
		AjaxResult result = new AjaxResult();
		
		// 不用设置授权目录域名
		// 统一下单地址
		// https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1#

		String ip = IpKit.getRealIp(request);
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}

		Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig().setAttach("IJPay 测试  -By Javen")
				.setBody("IJPay App付测试  -By Javen").setSpbillCreateIp(ip).setTotalFee("100")
				.setTradeType(WxPayApi.TradeType.APP).setNotifyUrl(notify_url)
				.setOutTradeNo(String.valueOf(System.currentTimeMillis())).build();

		String xmlResult = WxPayApi.pushOrder(false, params);

		log.info(xmlResult);
		Map<String, String> resultMap = PaymentKit.xmlToMap(xmlResult);

		String return_code = resultMap.get("return_code");
		String return_msg = resultMap.get("return_msg");
		if (!PaymentKit.codeIsOK(return_code)) {
			log.info(xmlResult);
			result.addError(return_msg);
			return result;
		}
		String result_code = resultMap.get("result_code");
		if (!PaymentKit.codeIsOK(result_code)) {
			log.info(xmlResult);
			result.addError(return_msg);
			return result;
		}
		// 以下字段在return_code 和result_code都为SUCCESS的时候有返回

		String prepay_id = resultMap.get("prepay_id");
		// 封装调起微信支付的参数
		// https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12

		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", WxPayApiConfigKit.getWxPayApiConfig().getAppId());
		packageParams.put("partnerid", WxPayApiConfigKit.getWxPayApiConfig().getMchId());
		packageParams.put("prepayid", prepay_id);
		packageParams.put("package", "Sign=WXPay");
		packageParams.put("noncestr", System.currentTimeMillis() + "");
		packageParams.put("timestamp", System.currentTimeMillis() / 1000 + "");
		String packageSign = PaymentKit.createSign(packageParams,
				WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey());
		packageParams.put("sign", packageSign);

		String jsonStr = JSON.toJSONString(packageParams);
		log.info("最新返回apk的参数:" + jsonStr);
		result.success(jsonStr);
		return result;
	}
	
	
	/**
	 * 退款
	 */
	@RequestMapping(value = "/tradeRefund")
	@ResponseBody
	public String tradeRefund(RefundRecord refundRecord) {
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", WxPayApiConfigKit.getWxPayApiConfig().getAppId());
		packageParams.put("mch_id", WxPayApiConfigKit.getWxPayApiConfig().getMchId());
		packageParams.put("nonce_str", System.currentTimeMillis() + "");
		packageParams.put("transaction_id", refundRecord.getTradeNo());
		packageParams.put("out_trade_no", refundRecord.getOutTradeNo());
		packageParams.put("out_refund_no", String.valueOf(System.currentTimeMillis()));
		packageParams.put("total_fee", String.valueOf(refundRecord.getTotalAmount().multiply(new BigDecimal(100)).intValue()));
		packageParams.put("refund_fee", String.valueOf(refundRecord.getRefundAmount().multiply(new BigDecimal(100)).intValue()));
		packageParams.put("refund_desc", refundRecord.getRefundReason());
		packageParams.put("notify_url", wxPayBean.getDomain().concat("/wxpay/refund_notify"));
		String packageSign = PaymentKit.createSign(packageParams,
				WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey());
		packageParams.put("sign", packageSign);
		String xmlResult = WxPayApi.orderRefund(false, packageParams, wxPayBean.getCertPath(), wxPayBean.getMchId());
		Map<String, String> resultMap = PaymentKit.xmlToMap(xmlResult);

		String return_code = resultMap.get("return_code");
		String return_msg = resultMap.get("return_msg");
		if (!PaymentKit.codeIsOK(return_code)) {
			log.info(xmlResult);
			return return_msg;
		}
		String result_code = resultMap.get("result_code");
		String err_code_des = resultMap.get("err_code_des");
		if (!PaymentKit.codeIsOK(result_code)) {
			log.info(xmlResult);
			return err_code_des;
		}
		refundRecord.setRefundStatus("SUCCESS");
		refundRecord.setRefundTime(new Date());
		refundRecord.setRecordTime(new Date());
		refundRecordMapper.insert(refundRecord);
		return "success";
	}
	

	@RequestMapping(value = "/pay_notify", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String pay_notify(HttpServletRequest request) {
		// 支付结果通用通知文档:
		// https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7

		String xmlMsg = HttpKit.readData(request);
		System.out.println("支付通知=" + xmlMsg);
		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
		 String appid = params.get("appid");
		 //商户号
		 String mch_id = params.get("mch_id");
		String result_code = params.get("result_code");
		 String openId = params.get("openid");
		 //交易类型
		 String trade_type = params.get("trade_type");
		 //付款银行
		 String bank_type = params.get("bank_type");
		 // 总金额
		 String total_fee = params.get("total_fee");
		 //现金支付金额
		 String cash_fee = params.get("cash_fee");
		 // 微信支付订单号
		 String transaction_id = params.get("transaction_id");
		 // 商户订单号
		 String out_trade_no = params.get("out_trade_no");
		 // 支付完成时间，格式为yyyyMMddHHmmss
		 String time_end = params.get("time_end");

		///////////////////////////// 以下是附加参数///////////////////////////////////

		String attach = params.get("attach");
		// String fee_type = params.get("fee_type");
		// String is_subscribe = params.get("is_subscribe");
		// String err_code = params.get("err_code");
		// String err_code_des = params.get("err_code_des");
		// 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
		// 避免已经成功、关闭、退款的订单被再次更新
		// Order order = Order.dao.getOrderByTransactionId(transaction_id);
		// if (order==null) {
		if (PaymentKit.verifyNotify(params, WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey())) {
			if (("SUCCESS").equals(result_code)) {
				// 更新订单信息
				log.warn("更新订单信息:" + attach);
				int count = payRecordMapper.getCountByOutTradeNo(out_trade_no);
				if(count == 0) {
					PayRecord record = new PayRecord();
					record.setBuyerId(openId);
					record.setBuyerName(params.get("buyer_logon_id"));
					record.setBuyerPayAmount(new BigDecimal(total_fee));
					record.setOutTradeNo(out_trade_no);
					record.setPayStatus(result_code);
					try {
						record.setPayTime(DateUtils.parseDate(time_end, "yyyyMMddHHmmss"));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					record.setPayWay("wxpay");
					if(attach != null) {
						String[] strArray = attach.replace("学球乐-", "").split("###");
						record.setProductId(Integer.parseInt(strArray[1]));
						record.setProductName(strArray[0]);
						if(strArray.length > 2)
							record.setUserName(strArray[2]);

					}
					
					record.setReceiptAmount(new BigDecimal(total_fee));
					record.setRecordTime(new Date());
					record.setSellerId(mch_id);
					record.setSellerName(mch_id);
					record.setTotalAmount(new BigDecimal(total_fee).divide(new BigDecimal(100)));
					record.setTradeNo(transaction_id);
					record.setUserId(openId);
					record.setOutTradeNo(out_trade_no);
					payRecordMapper.insert(record);
				}
				else
					log.info("重复接收到支付信息");
				// 发送通知等
				Map<String, String> xml = new HashMap<String, String>();
				xml.put("return_code", "SUCCESS");
				xml.put("return_msg", "OK");
				return PaymentKit.toXml(xml);
			}
		}
		// }

		return null;
	}
	
	@RequestMapping(value = "/refund_notify", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String refund_notify(HttpServletRequest request) {
		String xmlMsg = HttpKit.readData(request);
		System.out.println("退款通知=" + xmlMsg);
		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
		 String appid = params.get("appid");
		 //商户号
		 String mch_id = params.get("mch_id");
		String result_code = params.get("result_code");
		 String openId = params.get("openid");
		 //交易类型
		 String trade_type = params.get("trade_type");
		 //付款银行
		 String bank_type = params.get("bank_type");
		 // 总金额
		 String total_fee = params.get("total_fee");
		 //现金支付金额
		 String cash_fee = params.get("cash_fee");
		 // 微信支付订单号
		 String transaction_id = params.get("transaction_id");
		 // 商户订单号
		 String out_trade_no = params.get("out_trade_no");
		 // 支付完成时间，格式为yyyyMMddHHmmss
		 String time_end = params.get("time_end");

		///////////////////////////// 以下是附加参数///////////////////////////////////

		String attach = params.get("attach");
		// String fee_type = params.get("fee_type");
		// String is_subscribe = params.get("is_subscribe");
		// String err_code = params.get("err_code");
		// String err_code_des = params.get("err_code_des");
		// 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
		// 避免已经成功、关闭、退款的订单被再次更新
		// Order order = Order.dao.getOrderByTransactionId(transaction_id);
		// if (order==null) {
		if (PaymentKit.verifyNotify(params, WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey())) {
			if (("SUCCESS").equals(result_code)) {
				// 更新订单信息
				log.warn("更新订单信息:" + attach);
				int count = payRecordMapper.getCountByOutTradeNo(out_trade_no);
				if(count == 0) {
					PayRecord record = new PayRecord();
					record.setBuyerId(openId);
					record.setBuyerName(params.get("buyer_logon_id"));
					record.setBuyerPayAmount(new BigDecimal(total_fee));
					record.setOutTradeNo(out_trade_no);
					record.setPayStatus(result_code);
					try {
						record.setPayTime(DateUtils.parseDate(time_end, "yyyyMMddHHmmss"));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					record.setPayWay("wxpay");
					if(attach != null) {
						String[] strArray = attach.replace("学球乐-", "").split("###");
						record.setProductId(Integer.parseInt(strArray[1]));
						record.setProductName(strArray[0]);
						record.setUserName(strArray[2]);

					}
					
					record.setReceiptAmount(new BigDecimal(total_fee));
					record.setRecordTime(new Date());
					record.setSellerId(mch_id);
					record.setSellerName(mch_id);
					record.setTotalAmount(new BigDecimal(total_fee).divide(new BigDecimal(100)));
					record.setTradeNo(transaction_id);
					record.setUserId(openId);
					record.setOutTradeNo(out_trade_no);
					payRecordMapper.insert(record);
				}
				else
					log.info("重复接收到支付信息");
				// 发送通知等
				Map<String, String> xml = new HashMap<String, String>();
				xml.put("return_code", "SUCCESS");
				xml.put("return_msg", "OK");
				return PaymentKit.toXml(xml);
			}
		}
		// }

		return null;
	}

	@RequestMapping(value = "/return_url")
	@ResponseBody
	public String return_url(HttpServletRequest request) {
		try {

			String result = HttpKit.readData(request);
			System.out.println("callBack_xml>>>" + result);
			/**
			 * 
			 * 获取返回的信息内容中各个参数的值
			 * 
			 */
			Map<String, String> map = PaymentKit.xmlToMap(result);
			for (String key : map.keySet()) {
				System.out.println("key= " + key + " and value= " + map.get(key));
			}

			String appid = map.get("appid");
			String openid = map.get("openid");
			String mch_id = map.get("mch_id");
			String is_subscribe = map.get("is_subscribe");
			String nonce_str = map.get("nonce_str");
			String product_id = map.get("product_id");
			String sign = map.get("sign");
			Map<String, String> packageParams = new HashMap<String, String>();
			packageParams.put("appid", appid);
			packageParams.put("openid", openid);
			packageParams.put("mch_id", mch_id);
			packageParams.put("is_subscribe", is_subscribe);
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("product_id", product_id);

			String packageSign = PaymentKit.createSign(packageParams,
					WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey());
			// 统一下单文档地址：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1

			String ip = IpKit.getRealIp(request);
			if (StrKit.isBlank(ip)) {
				ip = "127.0.0.1";
			}
			ip = "127.0.0.1";
			List<BaseProduct> productList = baseProductMapper.getById(Integer.parseInt(product_id));
			if(productList.size() == 0) {
				return null;
			}
			BaseProduct product = productList.get(0);
			WxMpUser user = (WxMpUser) request.getSession().getAttribute("wxUser");
			String attach = "学球乐-" + product.getName() + "###" + product.getId() + "###" + (user != null ? user.getNickname() : "");
			String subject = "学球乐-" + product.getName();
			String totalAmount = product.getAmount().multiply(new BigDecimal(100)).intValue() + "";
			Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig().setAttach(attach)
					.setBody(subject).setOpenId(openid).setSpbillCreateIp(ip).setTotalFee(totalAmount)
					.setTradeType(TradeType.NATIVE).setNotifyUrl(notify_url)
					.setOutTradeNo(String.valueOf(System.currentTimeMillis())).build();

			String xmlResult = WxPayApi.pushOrder(false, params);
			log.info("prepay_xml>>>" + xmlResult);

			/**
			 * 
			 * 发送信息给微信服务器
			 * 
			 */
			Map<String, String> payResult = PaymentKit.xmlToMap(xmlResult);

			String return_code = payResult.get("return_code");
			String result_code = payResult.get("result_code");

			if (StrKit.notBlank(return_code) && StrKit.notBlank(result_code) && return_code.equalsIgnoreCase("SUCCESS")
					&& result_code.equalsIgnoreCase("SUCCESS")) {
				// 以下字段在return_code 和result_code都为SUCCESS的时候有返回

				String prepay_id = payResult.get("prepay_id");

				Map<String, String> prepayParams = new HashMap<String, String>();
				prepayParams.put("return_code", "SUCCESS");
				prepayParams.put("appId", appid);
				prepayParams.put("mch_id", mch_id);
				prepayParams.put("nonceStr", System.currentTimeMillis() + "");
				prepayParams.put("prepay_id", prepay_id);
				String prepaySign = null;
				if (sign.equals(packageSign)) {
					prepayParams.put("result_code", "SUCCESS");
				} else {
					prepayParams.put("result_code", "FAIL");
					prepayParams.put("err_code_des", "订单失效"); // result_code为FAIL时，添加该键值对，value值是微信告诉客户的信息

				}
				prepaySign = PaymentKit.createSign(prepayParams,
						WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey());
				prepayParams.put("sign", prepaySign);
				String xml = PaymentKit.toXml(prepayParams);
				log.error(xml);
				return xml;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
