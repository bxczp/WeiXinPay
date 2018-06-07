package com.bxczp.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.bxczp.config.WeixinPayConfig;
import com.bxczp.util.DateUtil;
import com.bxczp.util.HttpClientUtil;
import com.bxczp.util.Md5Util;
import com.bxczp.util.StringUtil;
import com.bxczp.util.XmlUtil;

/**
 * 订单退款申请
 * @author Administrator
 *
 */
public class TestRefund {

	private static String url="https://api.mch.weixin.qq.com/secapi/pay/refund";
	
	public static void main(String[] args) throws UnsupportedOperationException, ClientProtocolException, IOException {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("appid", WeixinPayConfig.appid); // 公众账号ID
		map.put("mch_id", WeixinPayConfig.mch_id); // 商户号
		map.put("transaction_id", "4200000094201804192059258077"); // 微信订单号
		//map.put("out_trade_no", "20180419105343760"); // 商户订单号
		map.put("nonce_str", StringUtil.getRandomString(30)); // 随机字符串
		map.put("out_refund_no", DateUtil.getCurrentDateStr()); // 商户退款单号
		map.put("total_fee", 100); // 订单金额
		map.put("refund_fee", 10); // 退款金额
		map.put("sign", getSign(map)); // 签名
		String xml=XmlUtil.genXml(map); 
		System.out.println(xml);
		InputStream in=HttpClientUtil.sendXMLDataByHttpsPost(url, xml).getEntity().getContent(); // 发现xml消息
		getElementValue(in);
	}
	
	/**
	 * 通过返回IO流获取支付地址
	 * @param in
	 * @return
	 */
	private static void getElementValue(InputStream in){
		SAXReader reader = new SAXReader();
        Document document=null;
		try {
			document = reader.read(in);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Element root = document.getRootElement();
        List<Element> childElements = root.elements();
        for (Element child : childElements) {
        	System.out.println(child.getName()+":"+child.getStringValue());
        }
	}
	
	/**
     * 微信支付签名算法sign
     */
    private static String getSign(Map<String,Object> map) {
        StringBuffer sb = new StringBuffer();
        String[] keyArr = (String[]) map.keySet().toArray(new String[map.keySet().size()]);//获取map中的key转为array
        Arrays.sort(keyArr);//对array排序
        for (int i = 0, size = keyArr.length; i < size; ++i) {
            if ("sign".equals(keyArr[i])) {
                continue;
            }
            sb.append(keyArr[i] + "=" + map.get(keyArr[i]) + "&");
        }
        sb.append("key=" + WeixinPayConfig.key);
        String sign = Md5Util.string2MD5(sb.toString());
        return sign;
    }
}
