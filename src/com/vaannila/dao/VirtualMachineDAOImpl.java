package com.vaannila.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.vaannila.domain.VirtualMachine;

public class VirtualMachineDAOImpl implements VirtualMachineDAO {
	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;

	@Override
	public void saveVirtualMachine(VirtualMachine vm) {
		// TODO Auto-generated method stub
		try {
			session.save(vm);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} 
	}
	
	@Override
	public void updateVirtualMachine(VirtualMachine vm) {
		// TODO Auto-generated method stub
		try{
			session.getTransaction().begin();
			SQLQuery query = session.createSQLQuery("Update VM_TABLE vm SET vm.cpuCapacity = "+ vm.getCpuCapacity() + " , vm.storageCapacity = " + vm.getStorageCapacity() + " , vm.ramCapacity = " + vm.getRamCapacity() + " , vm.isAvailable = " + vm.getIsAvailable() + " where vm.vm_id= " + vm.getVmId());		
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e){
			session.getTransaction().rollback();
			e.printStackTrace();	
		}
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<VirtualMachine> listVirtualMachines() {
		// TODO Auto-generated method stub
		List<VirtualMachine> vmList= new ArrayList<VirtualMachine>();
		try {
			vmList = session.createQuery("from VirtualMachine").list();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return vmList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VirtualMachine> listVirtualMachines(String cloudIdDescription) {
		// TODO Auto-generated method stub
		List<VirtualMachine> vmList= new ArrayList<VirtualMachine>();
		try {
			vmList = session.createQuery("from VirtualMachine where cloud_Id = '" + cloudIdDescription +"'").list() ; 
			//
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return vmList;
	}


}
