package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.BaseBean;

import dao.ChatDao;

public class DeleteChat extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int table_id = Integer.parseInt(request.getParameter("table_id"));
		ChatDao chatDao = new ChatDao();
		BaseBean baseBean = new BaseBean();
		if (chatDao.deleteChat(table_id)!=null) {
			baseBean.setStatus("success");
		}else{
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
