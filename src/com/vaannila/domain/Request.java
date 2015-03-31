package com.vaannila.domain;

public class Request {
	
	private String resources;
	private String cpu;
	private String storage;
	private String ram;
	private String userId;
	private String algo;
	private String numberOfRequest; 
	private String region ;
	private String turnAroundTime;
	private String hoursRequired;
	private String requestDateTime;
	private String sumOfResource;
	
	//String myCloudList;
	
	public String getResources() {
		return resources;
	}
	public String getTurnAroundTime() {
		return turnAroundTime;
	}
	public void setTurnAroundTime(String turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}
	public void setResources(String resources) {
		this.resources = resources;
	}
	
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getStorage() {
		return storage;
	}
	public void setStorage(String storage) {
		this.storage = storage;
	}
	public String getRam() {
		return ram;
	}
	public void setRam(String ram) {
		this.ram = ram;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAlgo() {
		return algo;
	}
	public void setAlgo(String algo) {
		this.algo = algo;
	}
	public String getNumberOfRequest() {
		return numberOfRequest;
	}
	public void setNumberOfRequest(String numberOfRequest) {
		this.numberOfRequest = numberOfRequest;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getHoursRequired() {
		return hoursRequired;
	}
	
	public void setHoursRequired(String hoursRequired) {
		this.hoursRequired = hoursRequired;
	}
	
	

}
