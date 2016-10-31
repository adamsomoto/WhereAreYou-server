package com.somoto.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.somoto.datastoreObjects.User;

public class UsersServlet extends HttpServlet {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try{
			String umidString = req.getPathInfo();
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
		catch(Exception e){
			resp.setContentType("text/plain");
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write(e.getMessage());
		}
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try{
			resp.setContentType("text/plain");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String umid = req.getParameter("umid");
			if(umid==null){
				resp.getWriter().write("'umid' is empty");
				return;
			}
			String latitude = req.getParameter("latitude");
			if(latitude==null){
				resp.getWriter().write("'latitude' is empty");
				return;
			}
			String longitude = req.getParameter("longitude");
			if(longitude==null){
				resp.getWriter().write("'longitude' is empty");
				return;
			}
			User user = ofy().load().type(User.class).filter("umid", umid).first().now();
			if(user==null){
				user = new User();
				user.umid = umid;
				user.creation_time = new Date();
			}
			user.latitude = latitude;
			user.longitude = longitude;
			resp.setContentType("text/json");
			resp.setStatus(HttpServletResponse.SC_OK);
			ofy().save().entity(user).now();	
		}
		catch(Exception e){
			resp.setContentType("text/plain");
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write(e.getMessage());
		}
	}
		
	
}