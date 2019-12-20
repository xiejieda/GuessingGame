package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.ChatBean;
import bean.TableBean;

import dao.ChatDao;

public class GetChat extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int table_id = Integer.parseInt(request.getParameter("table_id"));
		response.setContentType("text/html;charset=utf-8");
		ChatDao chatDao = new ChatDao();
		ArrayList<ChatBean> allinfo = new ArrayList<ChatBean>();
		allinfo = chatDao.getChat_Info(table_id);
		Gson gson = new Gson();
		try {
			response.getWriter().println(gson.toJson(allinfo));
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			response.getWriter().close();
		}
	}

}
