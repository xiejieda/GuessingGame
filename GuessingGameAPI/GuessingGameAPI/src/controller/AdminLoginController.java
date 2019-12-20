package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.BaseBean;
import bean.UserBean;

import com.google.gson.Gson;

import dao.UserDao;

public class AdminLoginController extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		response.setContentType("text/html;charset=utf-8");
		UserDao dao = new UserDao();
		BaseBean data = new BaseBean();
		UserBean user = new UserBean();
		if (dao.getloginstatus(username)==0) {
			if (dao.valid(username,password)) {
				int id = dao.getuserid(username);
				dao.changeloginstatus1(id);
				data.setStatus("success");
				data.setMessage("You are logined");
				user.setId(id);
				user.setUsername(username);
				data.setUser(user);
			}else {
				data.setStatus("failed");
				data.setMessage("Incorrect username or password");
			}
		}else{
			data.setStatus("failed_exist");
			data.setMessage("Someone has logged into this account");
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
