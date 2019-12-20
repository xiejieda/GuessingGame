package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.BaseBean;
import bean.UserBean;

import dao.UserDao;

public class AdminSigninController extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		response.setContentType("text/html;charset=utf-8");
		UserDao dao = new UserDao();
		BaseBean data = new BaseBean();
		UserBean user = new UserBean();
		if (dao.getuserid(username)!=0) {
			data.setStatus("failed");
			data.setMessage("This account already exists");
		}else if (dao.insertDataToDB(username, password)!=null) {
			data.setStatus("success");
			data.setMessage("You are signin");
			user.setId(dao.getuserid(username));
			user.setUsername(username);
			user.setPassword(password);
			data.setUser(user);
		}else {
			data.setStatus("failed");
			data.setMessage("unknown");
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
