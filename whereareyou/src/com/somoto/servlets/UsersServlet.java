package com.somoto.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.somoto.datastoreObjects.User;

public class UsersServlet extends HttpServlet {

	private static final Gson GSON = new Gson();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		//Map<?,?> map = req.getParameterMap();
		List<User> userList = ofy().load().type(User.class).list();
		String json = GSON.toJson(userList);
		//ofy().load().type(User.class).filter("umid", "")
		resp.setContentType("text/plain");
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.getWriter().write(json);
	}
	
	
}