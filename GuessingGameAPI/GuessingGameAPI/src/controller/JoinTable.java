package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.BaseBean;
import dao.TableDao;
import dao.UserDao;

public class JoinTable extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id_table = Integer.parseInt(request.getParameter("id_table"));
		int user = Integer.parseInt(request.getParameter("user"));
		int id = Integer.parseInt(request.getParameter("id"));
		TableDao dao = new TableDao();
		BaseBean data = new BaseBean();
		if (dao.join_table(id_table, user, id)!=null) {
			data.setStatus("success");
		}else {
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
