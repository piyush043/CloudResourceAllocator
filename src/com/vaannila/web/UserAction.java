package com.vaannila.web;

import java.util.ArrayList;
import java.util.List;

import net.spy.memcached.internal.OperationFuture;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.vaannila.dao.UserDAO;
import com.vaannila.dao.UserDAOImpl;
import com.vaannila.domain.User;

public class UserAction extends ActionSupport implements ModelDriven<User> 
{

	private static final long serialVersionUID = -6659925652584240539L;

	private User user = new User();
	private List<User> userList = new ArrayList<User>();
	private UserDAO userDAO = new UserDAOImpl();
	
	@Override
	public User getModel() {
		return user;
	}
		public String add()
	{
		memCache memcached = new memCache();
		OperationFuture<Boolean> t = memcached.addInMemCache(user.getId().toString(), 3600, user);
        System.out.println("Added Data in MemCached=" + t.getStatus());
		userDAO.saveUser(user);
		return SUCCESS;
	}
	
	public String list()
	{
		userList = userDAO.listUser();
		/*OperationFuture<Boolean> t = memcached.addInMemCache("userList", 3600, userDAO.listUser());
        System.out.println("Added Data in MemCached=" + t.getStatus());
		userList  = (List<User>) memcached.getFromMemCache("userList");*/
	
		return SUCCESS;
	}
		
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

}
