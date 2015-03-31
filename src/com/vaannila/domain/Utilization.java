package com.vaannila.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="utilization")
public class Utilization {

	private String cpu, ram, storage, no_of_req, start_time, end_time, exec_time, user_id, utilization_id, vm_id;
	private String algorithm, hoursRequired, requestDateTime, sumOfResource;
	
	@Column(name="cpu")
	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	@Column(name="ram")
	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	@Column(name="storage")
	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	@Column(name="no_of_req")
	public String getNo_of_req() {
		return no_of_req;
	}

	public void setNo_of_req(String no_of_req) {
		this.no_of_req = no_of_req;
	}

	@Column(name="start_time")
	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	@Column(name="end_time")
	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	@Column(name="exec_time")
	public String getExec_time() {
		return exec_time;
	}

	public void setExec_time(String exec_time) {
		this.exec_time = exec_time;
	}
	
	@Column(name="user_id")
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	@Id
	@GeneratedValue
	@Column(name="utilization_id")
	public String getUtilization_id() {
		return utilization_id;
	}

	public void setUtilization_id(String utilization_id) {
		this.utilization_id = utilization_id;
	}
	
	@Column (name = "vm_id")
	public String getVm_id() {
		return vm_id;
	}

	public void setVm_id(String vm_id) {
		this.vm_id = vm_id;
	}

	@Column (name = "algorithm")	
	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	@Column (name = "hoursRequired")	
	public String getHoursRequired() {
		return hoursRequired;
	}

	public void setHoursRequired(String hoursRequired) {
		this.hoursRequired = hoursRequired;
	}
	
	@Column (name = "requestDateTime")	
	public String getRequestDateTime() {
		return requestDateTime;
	}

	public void setRequestDateTime(String requestDateTime) {
		this.requestDateTime = requestDateTime;
	}

	@Column (name="sumOfResource")
	public String getSumOfResource() {
		return sumOfResource;
	}

	public void setSumOfResource(String sumOfResource) {
		this.sumOfResource = sumOfResource;
	}
}
