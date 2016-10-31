package com.somoto.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.somoto.datastoreObjects.User;

public class DeleteOldUsersServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try{	
			Date tenMinutesAgo = new Date(System.currentTimeMillis() - 10 * 60 * 1000);
			List<User> oldUsers = ofy().load().type(User.class).filter("creation_time <", tenMinutesAgo).list();			
			ofy().delete().entities(oldUsers);
			List<User> startedUsers = ofy().load().type(User.class).filter("creation_time !=", 0).list();
			List<User> inactiveUsers = new ArrayList<>();
			for(User iter : startedUsers){
				long inactiveTime = System.currentTimeMillis() - iter.last_update.getTime();
				if(inactiveTime>60*1000){
					inactiveUsers.add(iter);
				}
			}
			ofy().delete().entities(inactiveUsers);
			resp.setContentType("text/plain");
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write("Deleted "+oldUsers.size()+" users");
		}
		catch(Exception e){
			resp.setContentType("text/plain");
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write(e.getMessage());
		}
	}
	
}
