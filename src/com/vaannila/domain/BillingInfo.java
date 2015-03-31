package com.vaannila.domain;

public class BillingInfo {
	
	private String user_name;
	private String exec_time;
	private String no_of_resouces;
	private String bill_amount;
	private String cpu;
	private String ram;
	private String storage;
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getRam() {
		return ram;
	}
	public void setRam(String ram) {
		this.ram = ram;
	}
	public String getStorage() {
		return storage;
	}
	public void setStorage(String storage) {
		this.storage = storage;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getExec_time() {
		return exec_time;
	}
	public void setExec_time(String exec_time) {
		this.exec_time = exec_time;
	}
	public String getNo_of_resouces() {
		return no_of_resouces;
	}
	public void setNo_of_resouces(String no_of_resouces) {
		this.no_of_resouces = no_of_resouces;
	}
	public String getBill_amount() {
		return bill_amount;
	}
	public void setBill_amount(String bill_amount) {
		this.bill_amount = bill_amount;
	}
	
	

}
