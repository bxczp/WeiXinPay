package com.bxczp.config;

/**
 * 微信支付配置文件
 * @author Administrator
 *
 */
public class WeixinPayConfig {

public static final String appid=""; // 公众账号ID
	
	public static final String mch_id=""; // 商户号
	
	public static final String device_info="WEB"; // 设备号
	
	public static final String url="https://api.mch.weixin.qq.com/pay/unifiedorder"; // 支付请求地址
	
	public static final String notify_url="http://pay2.java1234.com/notifyUrl"; // 公众账号ID
	
	public static final String key=""; // 商户的key【API密钥】
}
