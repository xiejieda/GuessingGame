package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.google.gson.Gson;

import dao.RibbleDao;

import bean.RibbleBean;

public class GiveRibble extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		int table_id = Integer.parseInt(request.getParameter("table_id"));
		response.setContentType("text/html;charset=utf-8");
		RibbleDao ribbleDao = new RibbleDao();
		ArrayList<Integer> ribble_id = new ArrayList<Integer>();
		ribble_id = ribbleDao.getribble_id(table_id);
		RibbleBean ribbleBean = new RibbleBean();
		ArrayList<String> ribble = new ArrayList<String>();
		ribble = ribbleDao.getribble(ribble_id.get(id));
		if (ribble!=null) {
			ribbleBean.setTip(ribble.get(0));
			ribbleBean.setRibble(ribble.get(1));
			ribbleBean.setAnswer(ribble.get(2));
		}
		
		
		Gson gson = new Gson();
		try {
			response.getWriter().println(gson.toJson(ribbleBean));
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			response.getWriter().close();
		}
		
	}

}
