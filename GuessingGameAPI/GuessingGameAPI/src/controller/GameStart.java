package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.BaseBean;
import bean.RibbleBean;

import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import dao.RibbleDao;
import dao.TableDao;

public class GameStart extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int table_id =Integer.parseInt(request.getParameter("table_id"));
		TableDao dao = new TableDao();
		RibbleDao ribbleDao = new RibbleDao();
		BaseBean data = new BaseBean();
		Random random = new Random();
		ArrayList<Integer> list = new ArrayList<Integer>();
		int i;
		while (list.size()<10) {
			i = random.nextInt(20)+1;
			if (!list.contains(i)) {
				list.add(i);
			}
		}
		for (int j = 0; j < list.size(); j++) {
			ribbleDao.game_ribble(list.get(j),j+1,table_id);
		}
		if (dao.gameStart(table_id)!=null) {
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
