package com.bxczp.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.bxczp.config.WeixinPayConfig;
import com.bxczp.util.HttpClientUtil;
import com.bxczp.util.Md5Util;
import com.bxczp.util.StringUtil;
import com.bxczp.util.XmlUtil;

/**
 * 下载对账单测试
 * @author Administrator
 *
 */
public class TestDownLoadBill {

	private static String url="https://api.mch.weixin.qq.com/pay/downloadbill";
	
	public static void main(String[] args) throws UnsupportedOperationException, ClientProtocolException, IOException {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("appid", WeixinPayConfig.appid); // 公众账号ID
		map.put("mch_id", WeixinPayConfig.mch_id); // 商户号
		map.put("nonce_str", StringUtil.getRandomString(30)); // 随机字符串
		map.put("bill_date", "20180419"); // 对账单日期
		map.put("bill_type", "ALL"); // 账单类型
		map.put("sign", getSign(map)); // 签名
		String xml=XmlUtil.genXml(map); 
		System.out.println(xml);
		InputStream in=HttpClientUtil.sendXMLDataByPost(url, xml).getEntity().getContent(); // 发现xml消息
		StringBuffer out=new StringBuffer();
		byte []b=new byte[4096];
		for(int n;(n=in.read(b))!=-1;){
			out.append(new String(b,0,n));
		}
		System.out.println(out.toString());
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
