package com.vaannila.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "VM_TABLE")
public class VirtualMachine {
	private int vmId;
	private String vmIdDescription;
	private String cloudId;
	private int isAvailable;
	private int cpuCapacity;
	private int ramCapacity;
	private int storageCapacity;
	private String vmState;
	private Integer pheromonCount;
	private Integer minDistance;
	private Integer vmDistance;
	private Integer quality;
	
	@Id
	@GeneratedValue
	@Column(name = "VM_ID")
	public int getVmId() {
		return vmId;
	}

	public void setVmId(int vmId) {
		this.vmId = vmId;
	}

	@Column(name = "VM_ID_DESC")
	public String getVmIdDescription() {
		return vmIdDescription;
	}

	public void setVmIdDescription(String vmIdDescription) {
		this.vmIdDescription = vmIdDescription;
	}

	@Column(name = "CLOUD_ID")
	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	@Column(name = "ISAVAILABLE")
	public int getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(int isAvailable) {
		this.isAvailable = isAvailable;
	}

	@Column(name = "CPUCAPACITY")
	public int getCpuCapacity() {
		return cpuCapacity;
	}

	public void setCpuCapacity(int cpuCapacity) {
		this.cpuCapacity = cpuCapacity;
	}

	@Column(name = "RAMCAPACITY")
	public int getRamCapacity() {
		return ramCapacity;
	}

	public void setRamCapacity(int ramCapacity) {
		this.ramCapacity = ramCapacity;
	}

	@Column(name = "STORAGECAPACITY")
	public int getStorageCapacity() {
		return storageCapacity;
	}

	public void setStorageCapacity(int storageCapacity) {
		this.storageCapacity = storageCapacity;
	}

	@NotNull
	@Column(name = "VM_STATE")
	public String getVmState() {
		return vmState;
	}

	public void setVmState(String vmState) {
		this.vmState = vmState;
	}
	
	@NotNull
	@Column(name = "PHEROMON_COUNT")
	public int getPheromonCount() {
		return pheromonCount;
	}

	public void setPheromonCount(int pheromonCount) {
		this.pheromonCount = pheromonCount;
	}

	@NotNull
	@Column(name = "MINDISTANCE")
	public int getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(int minDistance) {
		this.minDistance = minDistance;
	}

	@Column(name = "VMDISTANCE")
	public int getVmDistance() {
		return vmDistance;
	}

	public void setVmDistance(int vmDistance) {
		this.vmDistance = vmDistance;
	}

	@NotNull
	@Column(name = "QUALITY")
	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	
}
