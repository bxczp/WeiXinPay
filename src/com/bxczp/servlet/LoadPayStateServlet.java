package com.bxczp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取支付状态
 * @author Administrator
 *
 */
public class LoadPayStateServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int i=0;
	private int result=0;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		i++;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(i==10){
			result=1;
		}
		System.out.println("i="+i);
		System.out.println("result="+result);
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter writer = resp.getWriter();
		writer.println(result);
		writer.flush();
		writer.close();
	}
	
	

}
