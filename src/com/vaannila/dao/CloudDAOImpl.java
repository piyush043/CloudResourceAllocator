package com.vaannila.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.vaannila.domain.Cloud;

public class CloudDAOImpl implements CloudDAO{

	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Cloud> listCloud() {
		// TODO Auto-generated method stub
		List<Cloud> cloudList= new ArrayList<Cloud>();
		try {
			cloudList = session.createQuery("from Cloud").list();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return cloudList;
	}
}
