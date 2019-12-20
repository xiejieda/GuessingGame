package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.BaseBean;

import dao.ChatDao;

public class SetChat extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		int table_id = Integer.parseInt(request.getParameter("table_id"));
		String name = request.getParameter("name");
		String time = request.getParameter("time");
		String data1 = request.getParameter("data");
		String data = new String(data1.getBytes("ISO-8859-1"),"UTF-8"); 
		response.setContentType("text/html;charset=utf-8");
		ChatDao chatDao = new ChatDao();
		BaseBean baseBean = new BaseBean();
		if (chatDao.setChat(table_id, name, time, data)!=null) {
			baseBean.setStatus("success");
		}else {
			baseBean.setStatus("failed");
		}
		Gson gson = new Gson();
		try {
			response.getWriter().println(gson.toJson(baseBean));
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			response.getWriter().close();
		}
	}

}
