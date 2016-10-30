package com.somoto.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
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
		String umidString = req.getParameter("umid");
		List<User> userList = new ArrayList<>();
		if(umidString==null){
			userList = ofy().load().type(User.class).list();
		}
		else{
			String[] split = umidString.split(",");
			for(String umid : split){
				User user = ofy().load().type(User.class).filter("umid", umid).first().now();
				userList.add(user);
			}

		}
		String json = GSON.toJson(userList);
		resp.setContentType("text/plain");
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.getWriter().write(json);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		User user = new User();
		user.umid = "1483";
		user.location = "32,35";
		ofy().save().entity(user).now();
		
	}
	
}