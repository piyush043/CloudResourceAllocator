package com.vaannila.dao;

import java.util.List;

import com.vaannila.domain.BillingInfo;
import com.vaannila.domain.Utilization;

public interface UtilizationDAO {

	void saveUtilization(Utilization utilization);

	List<BillingInfo> getAll();

	List<Utilization> getComparision();

}
