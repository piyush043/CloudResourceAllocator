package cloudUtils;

import java.util.ArrayList;
import java.util.List;

public class Cloud {
	int cloudId =0;
	int locationId = 0;
	List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
	
	public Cloud(int locationId, int cloudId)
	{
		this.cloudId = cloudId;
		this.locationId= locationId;
	}
	public List<VirtualMachine> getVmList() {
		return vmList;
	}
	public int getCloudId() {
		return cloudId;
	}
	public void setCloudId(int cloudId) {
		this.cloudId = cloudId;
	}
	public void setVmList(List<VirtualMachine> vmList) {
		this.vmList = vmList;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
 
}
