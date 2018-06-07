package com.bxczp.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Servlet implementation class LoadPayImageServlet
 */
public class LoadPayImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String orderNo=DateUtil.getCurrentDateStr(); // 生成订单号
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("appid", WeixinPayConfig.appid); // 公众账号ID
		map.put("mch_id", WeixinPayConfig.mch_id); // 商户号
		map.put("device_info", WeixinPayConfig.device_info); // 设备号
		map.put("notify_url", WeixinPayConfig.notify_url); // 异步通知地址
		map.put("trade_type", "NATIVE"); // 交易类型
		map.put("out_trade_no", orderNo); // 商户订单号
		map.put("body", "测试商品"); // 商品描述
		map.put("total_fee", 100); // 标价金额
		// map.put("spbill_create_ip", getRemortIP(request)); // 终端IP
		map.put("spbill_create_ip", "127.0.0.1"); // 终端IP
		map.put("nonce_str", StringUtil.getRandomString(30)); // 随机字符串
		map.put("sign", getSign(map)); // 签名
		String xml=XmlUtil.genXml(map); 
		System.out.println(xml);
		InputStream in=HttpClientUtil.sendXMLDataByPost(WeixinPayConfig.url, xml).getEntity().getContent(); // 发现xml消息
		String code_url=getElementValue(in,"code_url"); // 获取二维码地址
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();  
	    Map hints = new HashMap();  
	    BitMatrix bitMatrix = null;  
	    try {  
	         bitMatrix = multiFormatWriter.encode(code_url, BarcodeFormat.QR_CODE, 250, 250,hints);  
	         BufferedImage image = toBufferedImage(bitMatrix);  
	         //输出二维码图片流  
	         ImageIO.write(image, "png", response.getOutputStream());  
	     } catch (WriterException e1) {  
	         e1.printStackTrace();  
	     }  
	}

	/**
     * 获取本机IP地址
     * @return IP
     */
	private static String getRemortIP(HttpServletRequest request) {  
        if (request.getHeader("x-forwarded-for") == null) {  
            return request.getRemoteAddr();  
        }  
        return request.getHeader("x-forwarded-for");  
    }
	
	/**
     * 微信支付签名算法sign
     */
    private String getSign(Map<String,Object> map) {
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
    
    /**
	 * 通过返回IO流获取支付地址
	 * @param in
	 * @return
	 */
	private String getElementValue(InputStream in,String key){
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
        	if(key.equals(child.getName())){
        		return child.getStringValue();
        	}
        }
        return null;
	}
	
	/** 
	   * 类型转换 
	   * @author chenp 
	   * @param matrix 
	   * @return 
	   */  
	 public static BufferedImage toBufferedImage(BitMatrix matrix) {  
      int width = matrix.getWidth();  
      int height = matrix.getHeight();  
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);  
      for (int x = 0; x < width; x++) {  
          for (int y = 0; y < height; y++) {  
              image.setRGB(x, y, matrix.get(x, y) == true ? 0xff000000 : 0xFFFFFFFF);  
          }  
      }  
      return image;  
	 }
}
