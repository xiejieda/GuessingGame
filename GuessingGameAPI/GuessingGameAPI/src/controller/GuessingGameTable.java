package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.TableDao;

import bean.TableBean;

public class GuessingGameTable extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		TableDao dao = new TableDao();
		ArrayList<Integer> info = new ArrayList<Integer>();
		ArrayList<TableBean> allinfo = new ArrayList<TableBean>();
		for (int i = 1; i < 4; i++) {
			TableBean tableBean = new TableBean();
			info = dao.getTable_Info1(i);
			tableBean.setId(i);
			tableBean.setUser_1(info.get(0));
			tableBean.setUser_2(info.get(1));
			tableBean.setUser_3(info.get(2));
			tableBean.setUser_4(info.get(3));
			tableBean.setLast_check(dao.getTable_Info2(i));
			tableBean.setGame_id(info.get(4));
			allinfo.add(tableBean);
		}
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
