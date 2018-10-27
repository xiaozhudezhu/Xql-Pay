package com.swinginwind.xql.wechat.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.swinginwind.xql.pay.entity.LoginRecord;
import com.swinginwind.xql.pay.entity.WxPayBean;
import com.swinginwind.xql.pay.mapper.LoginRecordMapper;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

@Controller
@RequestMapping("/wechat")
public class WechatController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
	WxPayBean wxPayBean;
    
    @Resource(name = "wxMpService")
    private WxMpService wxMpService;
    
    @Resource(name = "wxWebService")
    private WxMpService wxWebService;

    @Autowired
    private WxMpMessageRouter router;
    
    @Autowired
    private LoginRecordMapper loginRecordMapper;
    
    
    /**
     * 访问这个时便会发起微信的网页授权
     * @param returnUrl 发起授权是可携带的一个参数，我这里用的是下面将要用到的login()的地址，将获取到的openid传递过去
     * @return
     * @throws UnsupportedEncodingException 
     */
    @GetMapping("/authorizeWap")
    public String authorizeWap(HttpServletRequest request) throws UnsupportedEncodingException {
    //设置回调地址
        String url = wxPayBean.getDomain().concat("/wechat/userInfoMp");
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, "STATE");
        return "redirect:" + redirectUrl;
    }
    
    /**
     * 访问这个时便会发起微信的网页授权
     * @param returnUrl 发起授权是可携带的一个参数，我这里用的是下面将要用到的login()的地址，将获取到的openid传递过去
     * @return
     * @throws UnsupportedEncodingException 
     */
    @GetMapping("/authorizePc")
    public String authorizePc(HttpServletRequest request) throws UnsupportedEncodingException {
    //设置回调地址
        String url = wxPayBean.getDomain().concat("/wechat/userInfoWeb");
        String redirectUrl = wxWebService.buildQrConnectUrl(url, WxConsts.QRCONNECT_SCOPE_SNSAPI_LOGIN, "STATE");
        return "redirect:" + redirectUrl;
    }
    
    
  //微信回调时访问的地址  这里获得code和之前所设置的returnUrl
    @GetMapping("/userInfoMp")
    public String userInfoMp(HttpServletRequest request, @RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        WxMpUser user = null;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            user = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            LoginRecord record = new LoginRecord(user);
            loginRecordMapper.insert(record);
            
        } catch (WxErrorException e) {
            e.printStackTrace();
        //抛出异常 自定义的  方便处理  可自己定义
            
        }
        if(user != null) {
        	request.getSession().setAttribute("wxUser", user);
        	request.getSession().setAttribute("openId", user.getOpenId());
        }
        return "redirect:/";
    }
    
    @GetMapping("/userInfoWeb")
    public String userInfoWeb(HttpServletRequest request, @RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        WxMpUser user = null;
        try {
            wxMpOAuth2AccessToken = wxWebService.oauth2getAccessToken(code);
            user = wxWebService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            LoginRecord record = new LoginRecord(user);
            loginRecordMapper.insert(record);
        } catch (WxErrorException e) {
            e.printStackTrace();
        //抛出异常 自定义的  方便处理  可自己定义
            
        }
        if(user != null) {
        	request.getSession().setAttribute("wxUser", user);
        	request.getSession().setAttribute("openId", user.getOpenId());
        }
        return "redirect:/";
    }
    
    //获取登录信息
    @RequestMapping(value = "/getCurrentWxUser",method = RequestMethod.GET)
    @ResponseBody
    public Object getCurrentWxUser(HttpServletRequest request){
        return  request.getSession().getAttribute("wxUser");
    }
    
    //获取登录信息
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }
    

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(
            @RequestParam(name = "signature",
                    required = false) String signature,
            @RequestParam(name = "timestamp",
                    required = false) String timestamp,
            @RequestParam(name = "nonce", required = false) String nonce,
            @RequestParam(name = "echostr", required = false) String echostr) {

        this.logger.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
                timestamp, nonce, echostr);

        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        if (this.wxMpService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "非法请求";
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam(name = "encrypt_type",
                               required = false) String encType,
                       @RequestParam(name = "msg_signature",
                               required = false) String msgSignature) {
        this.logger.info(
                "\n接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                signature, encType, msgSignature, timestamp, nonce, requestBody);

        if (!this.wxMpService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toXml();
        } else if ("aes".equals(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
                    requestBody, this.wxMpService.getWxMpConfigStorage(), timestamp,
                    nonce, msgSignature);
            this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage
                    .toEncryptedXml(this.wxMpService.getWxMpConfigStorage());
        }

        this.logger.debug("\n组装回复信息：{}", out);

        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.router.route(message);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

}
