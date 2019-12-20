package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.BaseBean;

import com.google.gson.Gson;

import dao.TableDao;

public class GameStop extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int table_id =Integer.parseInt(request.getParameter("table_id"));
		TableDao dao = new TableDao();
		BaseBean data = new BaseBean();
		if (dao.gameStop(table_id)!=null) {
			data.setStatus("success");
		}else{
			data.setStatus("failed");
		}
		Gson gson = new Gson();
		try {
			response.getWriter().println(gson.toJson(data));
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			response.getWriter().close();
		}
	}

}
