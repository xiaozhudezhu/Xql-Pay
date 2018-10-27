package com.swinginwind.xql.pay.adapter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.swinginwind.xql.pay.interceptor.AliPayInterceptor;
import com.swinginwind.xql.pay.interceptor.CharacterEncodInterceptor;
import com.swinginwind.xql.pay.interceptor.WxPayInterceptor;

@Configuration
public class IJPayWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
    	registry.addInterceptor(new CharacterEncodInterceptor()).addPathPatterns("/unionpay/**");
        registry.addInterceptor(new AliPayInterceptor()).addPathPatterns("/alipay/**");
        registry.addInterceptor(new WxPayInterceptor()).addPathPatterns("/wxpay/**","/wxsubpay/**");
        super.addInterceptors(registry);
    }
}

