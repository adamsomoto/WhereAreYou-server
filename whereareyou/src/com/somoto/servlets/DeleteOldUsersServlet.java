package com.somoto.servlets;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.somoto.datastoreObjects.User;

public class DeleteOldUsersServlet extends HttpServlet {

	//private static final Logger log = Logger.getLogger(DeleteOldUsersServlet.class.getName());
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try{	
			Date tenMinutesAgo = new Date(System.currentTimeMillis() - 10 * 60 * 1000);
			List<User> oldUsers = ofy().load().type(User.class).filter("creation_time <", tenMinutesAgo).list();
			removeTestUsersFromList(oldUsers);
			resp.getWriter().write("oldUsers "+oldUsers.size()+" users\n");
			ofy().delete().entities(oldUsers);
			List<User> allUsers = ofy().load().type(User.class).list();
			List<User> inactiveUsers = new ArrayList<>();
			for(User iter : allUsers){
				if(iter.lat==null){
					continue;
				}
				long inactiveTime = System.currentTimeMillis() - iter.last_update.getTime();
				if(inactiveTime>60*1000){
					inactiveUsers.add(iter);
				}
			}
			removeTestUsersFromList(inactiveUsers);
			resp.getWriter().write("inactiveUsers "+inactiveUsers.size()+" users");
			ofy().delete().entities(inactiveUsers);
			resp.setContentType("text/plain");
			resp.setStatus(HttpServletResponse.SC_OK);		
		}
		catch(Exception e){
			resp.setContentType("text/plain");
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write(e.getMessage());
		}
	}
	
	private static void removeTestUsersFromList(List<User> list){
		List<User> testUsers = new ArrayList<>();
		for(User iter : list){
			if(iter.umid.contains("test")){
				testUsers.add(iter);
			}
		}
		list.removeAll(testUsers);
	}
	
}
