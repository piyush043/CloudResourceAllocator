package com.vaannila.dao;

import java.util.List;

import com.vaannila.domain.VirtualMachine;

public interface VirtualMachineDAO {
	public void saveVirtualMachine(VirtualMachine vm);
	public List<VirtualMachine> listVirtualMachines ();
	public List<VirtualMachine> listVirtualMachines (String cloudIdDescription);
	public void updateVirtualMachine(VirtualMachine vm);
}