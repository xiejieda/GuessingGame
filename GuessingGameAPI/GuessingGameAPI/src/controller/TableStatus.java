package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.TableBean;
import bean.UserBean;

import dao.TableDao;

public class TableStatus extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int table_id =Integer.parseInt(request.getParameter("table_id"));
		response.setContentType("text/html;charset=utf-8");
		TableDao tableDao = new TableDao();
		TableBean tabledata = new TableBean();
		UserBean user1 = new UserBean();
		UserBean user2 = new UserBean();
		UserBean user3 = new UserBean();
		UserBean user4 = new UserBean();
		ArrayList<Integer> info = tableDao.getTable_userinfo(table_id);
		tabledata.setId(info.get(0));
		tabledata.setUser_1(info.get(1));
		tabledata.setUser_2(info.get(2));
		tabledata.setUser_3(info.get(3));
		tabledata.setUser_4(info.get(4));
		for (int i = 0; i < 4; i++) {
			if(info.get(i+1)>0&&i==0){
				ArrayList<String> userinfo = tableDao.getTable_userstatus(info.get(i+1));
				user1.setId(Integer.parseInt(userinfo.get(0)));
				user1.setUsername(userinfo.get(1));
				user1.setGame_status(Integer.parseInt(userinfo.get(2)));
				user1.setGame_grade(Integer.parseInt(userinfo.get(3)));
				tabledata.setUser_1_info(user1);
			}
			if(info.get(i+1)>0&&i==1){
				ArrayList<String> userinfo = tableDao.getTable_userstatus(info.get(i+1));
				user2.setId(Integer.parseInt(userinfo.get(0)));
				user2.setUsername(userinfo.get(1));
				user2.setGame_status(Integer.parseInt(userinfo.get(2)));
				user2.setGame_grade(Integer.parseInt(userinfo.get(3)));
				tabledata.setUser_2_info(user2);
			}
			if(info.get(i+1)>0&&i==2){
				ArrayList<String> userinfo = tableDao.getTable_userstatus(info.get(i+1));
				user3.setId(Integer.parseInt(userinfo.get(0)));
				user3.setUsername(userinfo.get(1));
				user3.setGame_status(Integer.parseInt(userinfo.get(2)));
				user3.setGame_grade(Integer.parseInt(userinfo.get(3)));
				tabledata.setUser_3_info(user3);
			}
			if(info.get(i+1)>0&&i==3){
				ArrayList<String> userinfo = tableDao.getTable_userstatus(info.get(i+1));
				user4.setId(Integer.parseInt(userinfo.get(0)));
				user4.setUsername(userinfo.get(1));
				user4.setGame_status(Integer.parseInt(userinfo.get(2)));
				user4.setGame_grade(Integer.parseInt(userinfo.get(3)));
				tabledata.setUser_4_info(user4);
			}
		}
		Gson gson = new Gson();
		try {
			response.getWriter().println(gson.toJson(tabledata));
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			response.getWriter().close();
		}
	}

}
