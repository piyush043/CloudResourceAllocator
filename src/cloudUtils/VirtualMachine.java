package cloudUtils;

public class VirtualMachine {
	

	int vmId = 0;
	int locationId = 0;
	int ramCapacity = 0;
	int cpuCapacity = 0;
	int storageCapacity = 0;
	boolean isAvailable = true;
	int pheromonCount = 0;
	int minDistance = 0;
	int vmDistance = 0;
	int quality = 0;
	
	public VirtualMachine(int locationId,int cpuCapacity,int ramCapacity,int storageCapacity,
						   boolean isAvailable,int pheromonCount,int minDistance,int vmDistance, int vmId,int quality)
	{
		 this.vmId=vmId;
		 this.locationId =locationId;
		 this.ramCapacity = ramCapacity;
		 this.cpuCapacity = cpuCapacity;
		 this.storageCapacity = storageCapacity;
		 this.isAvailable = isAvailable;
		 this.pheromonCount = pheromonCount;
		 this.minDistance = minDistance;
		 this.quality = quality;
		 this.vmDistance = vmDistance;
	}
	
	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getVmId() {
		return vmId;
	}

	public void setVmId(int vmId) {
		this.vmId = vmId;
	}

	public int getVmDistance() {
		return vmDistance;
	}

	public void setVmDistance(int vmDistance) {
		this.vmDistance = vmDistance;
	}

	public int getMinDistance() {
		return minDistance;
	}
	public void setMinDistance(int minDistance) {
		this.minDistance = minDistance;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
	public int getRamCapacity() {
		return ramCapacity;
	}

	public void setRamCapacity(int ramCapacity) {
		this.ramCapacity = ramCapacity;
	}

	public int getCpuCapacity() {
		return cpuCapacity;
	}
	public void setCpuCapacity(int cpuCapacity) {
		this.cpuCapacity = cpuCapacity;
	}
	public int getStorageCapacity() {
		return storageCapacity;
	}
	public void setStorageCapacity(int storageCapacity) {
		this.storageCapacity = storageCapacity;
	}
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	public int getPheromonCount() {
		return pheromonCount;
	}
	public void setPheromonCount(int pheromonCount) {
		this.pheromonCount = pheromonCount;
	}
	
	
}
