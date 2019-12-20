package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.BaseBean;
import dao.UserDao;

public class UserStatus extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id =Integer.parseInt(request.getParameter("id"));
		int status = Integer.parseInt(request.getParameter("status"));
		UserDao dao = new UserDao();
		BaseBean data = new BaseBean();
		if (dao.changgamestatus(id, status)!=null) {
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
