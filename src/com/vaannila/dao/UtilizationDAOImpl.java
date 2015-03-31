package com.vaannila.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.vaannila.domain.BillingInfo;
import com.vaannila.domain.User;
import com.vaannila.domain.Utilization;

public class UtilizationDAOImpl implements UtilizationDAO{
	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;
	
	
		
	
	@Override
	public void saveUtilization(Utilization utilization) {
		try {
			session.save(utilization);
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} 
	}
	
	@Override
	public List<BillingInfo> getAll() {
	
		
		List<BillingInfo> listBill = new ArrayList<BillingInfo>();
		SQLQuery query = session.createSQLQuery("SELECT user.user_name,sum(utilization.exec_time),sum(utilization.cpu),sum(utilization.ram),sum(utilization.storage),sum(utilization.exec_time)*0.2 FROM cloudloadbalancer.user user,cloudloadbalancer.utilization utilization where user.user_id = utilization.user_id group by user.user_id");
		List<Object[]> rows  = query.list();
		for(Object[] row : rows){
			BillingInfo bill = new BillingInfo();
			bill.setUser_name(row[0].toString());
			bill.setExec_time((row[1].toString()));
		    bill.setCpu((row[2].toString()));
		    bill.setRam((row[3].toString()));
		    bill.setStorage((row[4].toString()));
		    bill.setBill_amount((row[5].toString()));
		    listBill.add(bill);
		}
		return listBill;
	}
	
	
	@Override
	public List<Utilization> getComparision() {
	
		
		List<Utilization> listUtilizations= new ArrayList<Utilization>();
		SQLQuery query = session.createSQLQuery("SELECT"
				+ "    utilization.algorithm,"
				+ "    SUM(utilization.exec_time) / SUM(utilization.sumOfResource)"
				+ " FROM"
				+ "    cloudloadbalancer.utilization utilization"
				+ " GROUP BY utilization.algorithm");
		List<Object[]> rows  = query.list();
		for(Object[] row : rows){
			Utilization utilization = new Utilization();
			utilization.setAlgorithm(row[0].toString());
			utilization.setExec_time((row[1].toString()));
		    listUtilizations.add(utilization);
		}
		return listUtilizations;
	}
}
