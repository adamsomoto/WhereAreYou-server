package com.somoto.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.somoto.datastoreObjects.User;

public class UsersServlet extends HttpServlet {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
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
		String body = IOUtils.toString(req.getInputStream(), "UTF-8");
		User user = GSON.fromJson(body, User.class);		
		ofy().save().entity(user).now();		
	}
	
}