package com.vaannila.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.vaannila.domain.VirtualMachine;

@Entity
@Table(name="CLOUD_TABLE")
public class Cloud{

	private int cloudId;
	private String cloudIdDescription;
	private int cloudLocation;
	private List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		
	@Id
	@GeneratedValue
	@Column(name="CLOUD_ID")
	public int getCloudId() {
		return cloudId;
	}
	public void setCloudId(int cloudId) {
		this.cloudId = cloudId;
	}
	
	
	@Column(name="CLOUD_ID_DESC")
	public String getCloudIdDescription() {
		return cloudIdDescription;
	}
	public void setCloudIdDescription(String cloudIdDescription) {
		this.cloudIdDescription = cloudIdDescription;
	}
	
	@Column(name="CLOUD_LOCATION")
	public int getCloudLocation() {
		return cloudLocation;
	}
	public void setCloudLocation(int cloudLocation) {
		this.cloudLocation = cloudLocation;
	}
	
	@Transient
	public List<VirtualMachine> getVmList() {
		return vmList;
	}
	public void setVmList(List<VirtualMachine> vmList) {
		this.vmList = vmList;
	}
	
	
}
	