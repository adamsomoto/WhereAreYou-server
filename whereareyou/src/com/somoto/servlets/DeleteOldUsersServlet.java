package com.somoto.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.somoto.datastoreObjects.User;

public class DeleteOldUsersServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try{	
			Date date = new Date(System.currentTimeMillis() - 30 * 60 * 1000);
			List<User> oldUsers = ofy().load().type(User.class).filter("creation_time <", date).list();
			ofy().delete().entities(oldUsers);
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
