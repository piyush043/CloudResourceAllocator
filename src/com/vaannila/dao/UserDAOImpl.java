package com.vaannila.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.vaannila.domain.User;

public class UserDAOImpl implements UserDAO {

	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;

	@SuppressWarnings("unchecked")
	@Override
	public List<User> listUser() {	
		List<User> courses = null;
		try {
			courses = session.createQuery("from User").list();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return courses;
	}

	@Override
	public void saveUser(User user) {
		try {
			session.save(user);
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} 
	}

}
