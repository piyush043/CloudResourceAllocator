package com.vaannila.web;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import net.spy.memcached.internal.OperationFuture;

import com.amazonaws.services.ec2.model.Instance;
import com.fasterxml.jackson.databind.util.Converter;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.sun.java_cup.internal.runtime.virtual_parse_stack;
import com.vaannila.dao.CloudDAO;
import com.vaannila.dao.CloudDAOImpl;
import com.vaannila.dao.UtilizationDAO;
import com.vaannila.dao.UtilizationDAOImpl;
import com.vaannila.dao.VirtualMachineDAO;
import com.vaannila.dao.VirtualMachineDAOImpl;
import com.vaannila.domain.BillingInfo;
import com.vaannila.domain.Request;
import com.vaannila.domain.User;
import com.vaannila.domain.Utilization;
import com.vaannila.domain.Cloud;
import com.vaannila.domain.VirtualMachine;

public class loadBalancerAction extends ActionSupport implements
		ModelDriven<Request> {

	private static final long serialVersionUID = 3593883582027045247L;
	private Request myReq = new Request();
	private UtilizationDAO utilizationDAO = new UtilizationDAOImpl();
	Utilization utilization = new Utilization();

	private CloudDAO cloudDAO = new CloudDAOImpl();
	Cloud cloud = new Cloud();

	private VirtualMachineDAO vmDAO = new VirtualMachineDAOImpl();
	VirtualMachine vm = new VirtualMachine();

	private List<BillingInfo> allBillingInfo = new ArrayList<BillingInfo>();
	private List<VirtualMachine> myVMList = new ArrayList<VirtualMachine>();
	private AmazonMachine awsMachine = new AmazonMachine();
	// private List<Instance> instanceList = new ArrayList<Instance>();
	private List<Cloud> myCloudList = new ArrayList<Cloud>();
	private List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
	public boolean firstTime = true;
	public static int cpuCapacity = 5000;
	public static int ramCapacity = 500000;
	public static int storageCapacity = 50000;

	private static String INSTANCE_TYPE_MICRO = "t2.micro";
	private static String IMAGE_ID_AMAZON_LINUX_AMI = "ami-b5a7ea85";
	private static String IMAGE_ID_WINDOWS_SERVER_2012_RTM = "ami-51f0bc61";
	private static String IMAGE_ID_CUSTOM_WINDOWS = "ami-896f19b9";
	private static String INSTANCE_TYPE_SMALL = "m1.small";
	private static String INSTANCE_TYPE_LARGE = "g2.2xlarge";
	private InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void loginIntoAWS() throws Exception {

		awsMachine.getCredentials();
		// instanceList = awsMachine.getInstanceList(); //
	}

	public List<VirtualMachine> getMyVMList() {
		return myVMList;
	}

	public void setMyVMList(List<VirtualMachine> myVMList) {
		this.myVMList = myVMList;
	}

	public List<VirtualMachine> getVmList() {
		return vmList;
	}

	public void setVmList(List<VirtualMachine> vmList) {
		this.vmList = vmList;
	}

	public List<Cloud> getMyCloudList() {
		return myCloudList;
	}

	public void setMyCloudList(List<Cloud> myCloudList) {
		this.myCloudList = myCloudList;
	}

	public List<BillingInfo> getAllBillingInfo() {
		return allBillingInfo;
	}

	public void setAllBillingInfo(List<BillingInfo> allBillingInfo) {
		this.allBillingInfo = allBillingInfo;
	}

	public Request getMyReq() {
		return myReq;
	}

	public void setMyReq(Request myReq) {
		this.myReq = myReq;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	public loadBalancerAction() {
		super();
	}

	public String open() {
		return SUCCESS;
	}

	public String billing() {
		allBillingInfo = utilizationDAO.getAll();
		return SUCCESS;
	}

	public void getCloudandVMList() {
		myCloudList = cloudDAO.listCloud();
		for (int i = 0; i < myCloudList.size(); i++) {
			vmList = vmDAO.listVirtualMachines(myCloudList.get(i)
					.getCloudIdDescription());
			myCloudList.get(i).setVmList(vmList);
		}
	}

	public String allocate() throws Exception {
		System.out.println("in allocate");

		Map session = ActionContext.getContext().getSession();
		myReq = (Request) session.get("myReq");

		getCloudandVMList();

		setMyReq(myReq);

		String cpu = myReq.getCpu();
		String ram = myReq.getRam();
		String storage = myReq.getStorage();
		String numberOfRequest = myReq.getNumberOfRequest();
		String region = myReq.getRegion();
		String algo = myReq.getAlgo();

		
		// String resources = myReq.getResources();
		System.out.println("algo: " + myReq.getAlgo());
		System.out.println("Region: " + region + "\t Number of Request: "
				+ numberOfRequest + "\t Algorithm Selected:  " + algo
				+ "\t Cpu Requested: " + cpu + "\t RAM Requested: " + ram
				+ "\t Storage Requested: " + storage);

		long startTime = new Date().getTime();
		System.out.println("Time in (ms):\t" + startTime);
		for (int i = 0; i < Integer.parseInt(numberOfRequest); i++) {
			loadBalancer(algo, cpu, ram, storage, region);
		}
		long endTime = new Date().getTime();
		System.out.println("time in milliseconds" + endTime);
		long turnAroundTime = endTime - startTime;
		myReq.setTurnAroundTime(String.valueOf(turnAroundTime));
		utilization.setUser_id(myReq.getUserId());
		utilization.setExec_time(myReq.getTurnAroundTime());
		utilization.setCpu(cpu);
		utilization.setRam(ram);
		utilization.setStorage(storage);
		// utilization.setResource(myReq.getResources());
		utilization.setNo_of_req(numberOfRequest);
		utilization.setAlgorithm(algo);
		DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		utilization.setRequestDateTime(dateformat.format(date));
		int sumOfResource = Integer.parseInt(cpu) + Integer.parseInt(ram)
				+ Integer.parseInt(storage);
		utilization.setSumOfResource(String.valueOf(sumOfResource));
		utilization.setHoursRequired(myReq.getHoursRequired());
		utilizationDAO.saveUtilization(utilization);

		
		return SUCCESS;

	}

	@Override
	public Request getModel() {
		// TODO Auto-generated method stub
		return myReq;
	}

	public void loadBalancer(String algo, String cpu, String ram,
			String storage, String region) throws Exception {
		// Calendar lCDateTime = Calendar.getInstance();
		// System.out.println("time in milliseconds"+
		// lCDateTime.getTimeInMillis());
		try {

			if (algo.equals("ant")) {
				antColonyAlgo(cpu, ram, storage, myCloudList);
			} else if (algo.equals("loc")) {
				locationAwareAlgo(cpu, ram, storage, myCloudList, region);
			} else if (algo.equals("pso")) {
				psoAlgo(cpu, ram, storage, myCloudList);
			} else {
				honeyBeeAlgo(cpu, ram, storage, myCloudList);
			}
		} catch (Exception e) {
			System.out.println("hahaha..." + e);
			e.printStackTrace();
		}

	}

	public void locationAwareAlgo(String cpu, String ram, String storage,
			List<Cloud> myCloudList, String region) throws Exception {
		int reqRegion = Integer.parseInt(region);
		int regDiff = 0;
		int regDiffAbs = 0;
		int minDis = Integer.MAX_VALUE;
		int cloudId = 0, vmCapacity, reqCapDiff;
		int reqCpu, reqStorage, reqRam;
		int newCpuCapacity = 0;
		int newRamCapacity = 0;
		int newStorageCapacity = 0;
		int vmCpuCapacity = 0;
		int vmStorageCapacity = 0;
		int vmRamCapacity = 0;

		reqCpu = Integer.parseInt(cpu);
		reqStorage = Integer.parseInt(storage);
		reqRam = Integer.parseInt(ram);
		Cloud currentCloud = null;
		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		for (int i = 0; i < myCloudList.size(); i++) {

			Cloud tempCloud = myCloudList.get(i);
			List<VirtualMachine> tempVMList = tempCloud.getVmList();
			int availableCloud = 0;
			for (int k = 0; k < tempVMList.size(); k++) {
				if (tempVMList.get(k).getIsAvailable() == 1) {
					availableCloud++;
				}
			}
			// finding the cloud -- best min distance
			regDiff = reqRegion - myCloudList.get(i).getCloudLocation();
			regDiffAbs = Math.abs(regDiff);
			if (regDiffAbs <= minDis && availableCloud > 0) {
				minDis = regDiffAbs;
				currentCloud = myCloudList.get(i);
				cloudId = myCloudList.get(i).getCloudId();
			}
		}
		vmList.addAll(currentCloud.getVmList());

		int minVMDist = Integer.MAX_VALUE, currentVMDist;
		int currentVMId = 0;

		VirtualMachine currentVM = null;

		for (int j = 0; j < vmList.size(); j++) {

			currentVMDist = vmList.get(j).getVmDistance();
			vmCpuCapacity = vmList.get(j).getCpuCapacity();
			vmRamCapacity = vmList.get(j).getRamCapacity();
			vmStorageCapacity = vmList.get(j).getStorageCapacity();

			// checking the capacity of the vm

			if (currentVMDist <= minVMDist && vmCpuCapacity > reqCpu
					&& vmRamCapacity > reqRam && vmStorageCapacity > reqStorage) {
				minVMDist = currentVMDist;
				currentVMId = vmList.get(j).getVmId();
				currentVM = vmList.get(j);
			}
		}
		System.out.println("currentVMId-->" + currentVMId);

		// VirtualMachine currentVM = vmList.get(currentVMId);

		vmCpuCapacity = currentVM.getCpuCapacity();
		vmRamCapacity = currentVM.getRamCapacity();
		vmStorageCapacity = currentVM.getStorageCapacity();

		// checking the capacity of the vm
		newCpuCapacity = vmCpuCapacity - reqCpu;
		newRamCapacity = vmRamCapacity - reqRam;
		newStorageCapacity = vmStorageCapacity - reqStorage;

		if (newCpuCapacity >= 0 && newRamCapacity >= 0
				&& newStorageCapacity >= 0) {
			currentVM.setCpuCapacity(newCpuCapacity);
			currentVM.setRamCapacity(newRamCapacity);
			currentVM.setStorageCapacity(newStorageCapacity);

			if (newCpuCapacity == 0 || newRamCapacity == 0
					|| newStorageCapacity == 0) {
				currentVM.setIsAvailable(0);
				System.out.println("vmid " + currentVM.getVmId() + " full");
			}

			vmDAO.updateVirtualMachine(currentVM);

		} else {

			System.out
					.println("No VM Available in this Cloud. \n Creating New Instance.... Please wait.....\n");
			awsMachine.getCredentials();

			String[] instanceDetails = awsMachine.createInstance(
					IMAGE_ID_AMAZON_LINUX_AMI, "SecurityGroup2",
					"Mobile Security Group2", "AWSWindowsKey2");
			String instanceId = instanceDetails[0];
			String vpcId = instanceDetails[1];
			VirtualMachine vm = new VirtualMachine();
			vm.setVmIdDescription(instanceId);
			vm.setCloudId(vpcId);
			vm.setIsAvailable(1);
			vm.setCpuCapacity(500);
			vm.setStorageCapacity(100000);
			vm.setRamCapacity(10000);
			vm.setVmState("running");
			vm.setPheromonCount(1);
			vm.setMinDistance(minDis);
			vm.setVmDistance(1);
			vm.setQuality(1);
			vmDAO.saveVirtualMachine(vm);
			getCloudandVMList();
			locationAwareAlgo(cpu, ram, storage, myCloudList, region);
		}
		List<VirtualMachine> vmList2 = new ArrayList<VirtualMachine>();

		for (int i = 0; i < myCloudList.size(); i++) {
			vmList2.addAll(myCloudList.get(i).getVmList());
		}
		myVMList = new ArrayList<VirtualMachine>();
		for (int i = 0; i < vmList2.size(); i++) {
			myVMList.add(vmList2.get(i));
			System.out.println("vm id: " + vmList2.get(i).getVmId()
					+ "\t cpuCapacity: " + vmList2.get(i).getCpuCapacity()
					+ "\t ramCapacity: " + vmList2.get(i).getRamCapacity()
					+ "\t storageCapacity: "
					+ vmList2.get(i).getStorageCapacity());
		}
	}

	public void psoAlgo(String cpu, String ram, String storage,
			List<Cloud> myCloudList) throws Exception {
		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		int gBest = 0;
		int pBest = 0;

		int cloudCpuCapacity = 0;
		int cloudRamCapacity = 0;
		int cloudStorageCapacity = 0;

		// int storageCapacity = 0;
		int vmCpuCapacity = 0;
		int vmStorageCapacity = 0;
		int vmRamCapacity = 0;
		int reqCpu, reqStorage, reqRam;
		int newCpuCapacity = 0;
		int newRamCapacity = 0;
		int newStorageCapacity = 0;

		reqCpu = Integer.parseInt(cpu);
		reqStorage = Integer.parseInt(storage);
		reqRam = Integer.parseInt(ram);

		for (int i = 0; i < myCloudList.size(); i++) {
			int[] cloudCapacity = getCloudCapacity(myCloudList.get(i));

			cloudCpuCapacity = cloudCapacity[0];
			cloudStorageCapacity = cloudCapacity[1];
			cloudRamCapacity = cloudCapacity[2];

			if (gBest < cloudCpuCapacity) {
				vmList = new ArrayList<VirtualMachine>();
				gBest = cloudCpuCapacity;
				// myCloudList.get(i).getCloudId();
				vmList.addAll(myCloudList.get(i).getVmList());
				// cloudId = myCloudList.get(i).getCloudId();
			}
		}

		VirtualMachine currentVM = null;

		for (int i = 0; i < vmList.size(); i++) {
			vmCpuCapacity = vmList.get(i).getCpuCapacity();
			vmStorageCapacity = vmList.get(i).getStorageCapacity();
			vmRamCapacity = vmList.get(i).getRamCapacity();

			if (pBest < vmCpuCapacity && vmCpuCapacity > reqCpu
					&& vmRamCapacity > reqRam && vmStorageCapacity > reqStorage) {
				pBest = vmCpuCapacity;
				currentVM = vmList.get(i);

			}
		}
		if (currentVM != null) {
			vmCpuCapacity = currentVM.getCpuCapacity();
			vmStorageCapacity = currentVM.getStorageCapacity();
			vmRamCapacity = currentVM.getRamCapacity();

			newCpuCapacity = vmCpuCapacity - reqCpu;
			newRamCapacity = vmRamCapacity - reqRam;
			newStorageCapacity = vmStorageCapacity - reqStorage;

			if (newCpuCapacity >= 0 && newRamCapacity >= 0
					&& newStorageCapacity >= 0) {
				currentVM.setCpuCapacity(newCpuCapacity);
				currentVM.setStorageCapacity(newStorageCapacity);
				currentVM.setRamCapacity(newRamCapacity);
				if (newCpuCapacity == 0 || newRamCapacity == 0
						|| newStorageCapacity == 0) {
					currentVM.setIsAvailable(0);
					System.out.println("vmid " + currentVM.getVmId() + " full");
				}

				vmDAO.updateVirtualMachine(currentVM);
			}
		} else {

			System.out
					.println("No VM Available in this Cloud. \n Creating New Instance.... Please wait.....\n");
			awsMachine.getCredentials();

			String[] instanceDetails = awsMachine.createInstance(
					IMAGE_ID_AMAZON_LINUX_AMI, "SecurityGroup",
					"Mobile Security Group2", "AWSWindowsKey2");
			String instanceId = instanceDetails[0];
			String vpcId = instanceDetails[1];
			VirtualMachine vm = new VirtualMachine();
			vm.setVmIdDescription(instanceId);
			vm.setCloudId(vpcId);
			vm.setIsAvailable(1);
			vm.setCpuCapacity(500);
			vm.setStorageCapacity(100000);
			vm.setRamCapacity(10000);
			vm.setVmState("running");
			vm.setPheromonCount(1);
			vm.setMinDistance(1);
			vm.setVmDistance(1);
			vm.setQuality(1);
			vmDAO.saveVirtualMachine(vm);
			getCloudandVMList();
			psoAlgo(cpu, ram, storage, myCloudList);
		}

		List<VirtualMachine> vmList2 = new ArrayList<VirtualMachine>();

		for (int i = 0; i < myCloudList.size(); i++) {
			vmList2.addAll(myCloudList.get(i).getVmList());
		}

		myVMList = new ArrayList<VirtualMachine>();

		for (int i = 0; i < vmList2.size(); i++) {
			System.out.println("vm id: " + vmList2.get(i).getVmId()
					+ "\t cpuCapacity: " + vmList2.get(i).getCpuCapacity()
					+ "\t ramCapacity: " + vmList2.get(i).getRamCapacity()
					+ "\t storageCapacity: "
					+ vmList2.get(i).getStorageCapacity());

			myVMList.add(vmList2.get(i));
		}
	}

	public int[] getCloudCapacity(Cloud c) {
		int[] cloudCapacity = new int[3];
		cloudCapacity[0] = 0;
		cloudCapacity[1] = 0;
		cloudCapacity[2] = 0;
		// int vmId = 0;
		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		vmList.addAll(c.getVmList());
		for (int i = 0; i < vmList.size(); i++) {
			cloudCapacity[0] = cloudCapacity[0]
					+ vmList.get(i).getCpuCapacity();
			cloudCapacity[1] = cloudCapacity[1]
					+ vmList.get(i).getStorageCapacity();
			cloudCapacity[2] = cloudCapacity[2]
					+ vmList.get(i).getRamCapacity();
		}
		return cloudCapacity;
	}

	public void antColonyAlgo(String cpu, String ram, String storage,
			List<Cloud> myCloudList) throws Exception {
		VirtualMachine currentVM = selectVMAnt(cpu, ram, storage, myCloudList);
		int pheromonCount = 0;
		int newCpuCapacity = 0;
		int newRamCapacity = 0;
		int newStorageCapacity = 0;

		if (currentVM != null) {
			pheromonCount = currentVM.getPheromonCount();
			pheromonCount++;
			currentVM.setPheromonCount(pheromonCount);

			newCpuCapacity = currentVM.getCpuCapacity() - Integer.parseInt(cpu);
			currentVM.setCpuCapacity(newCpuCapacity);

			newRamCapacity = currentVM.getRamCapacity() - Integer.parseInt(ram);
			currentVM.setRamCapacity(newRamCapacity);

			newStorageCapacity = currentVM.getStorageCapacity()
					- Integer.parseInt(storage);
			currentVM.setStorageCapacity(newStorageCapacity);

			if (newCpuCapacity == 0 || newRamCapacity == 0
					|| newStorageCapacity == 0) {
				currentVM.setIsAvailable(0);
				System.out.println("vmid " + currentVM.getVmId() + " full");
			} else {
				System.out.println("location id--> " + currentVM.getCloudId()
						+ " vm Id --> " + currentVM.getVmId());
			}

			vmDAO.updateVirtualMachine(currentVM);

		} else {

			System.out
					.println("No VM Available in this Cloud. \n Creating New Instance.... Please wait.....\n");
			awsMachine.getCredentials();

			String[] instanceDetails = awsMachine.createInstance(
					IMAGE_ID_AMAZON_LINUX_AMI, "SecurityGroup2",
					"Mobile Security Group2", "AWSWindowsKey");
			String instanceId = instanceDetails[0];
			String vpcId = instanceDetails[1];
			VirtualMachine vm = new VirtualMachine();
			vm.setVmIdDescription(instanceId);
			vm.setCloudId(vpcId);
			vm.setIsAvailable(1);
			vm.setCpuCapacity(500);
			vm.setStorageCapacity(100000);
			vm.setRamCapacity(10000);
			vm.setVmState("running");
			vm.setPheromonCount(1);
			vm.setMinDistance(1);
			vm.setVmDistance(1);
			vm.setQuality(1);
			vmDAO.saveVirtualMachine(vm);
			getCloudandVMList();
			antColonyAlgo(cpu, ram, storage, myCloudList);
		}
		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();

		for (int i = 0; i < myCloudList.size(); i++) {
			vmList.addAll(myCloudList.get(i).getVmList());
		}
		myVMList = new ArrayList<VirtualMachine>();
		for (int i = 0; i < vmList.size(); i++) {
			System.out.println("vm id: " + vmList.get(i).getVmId()
					+ "\t cpuCapacity: " + vmList.get(i).getCpuCapacity()
					+ "\t ramCapacity: " + vmList.get(i).getRamCapacity()
					+ "\t storageCapacity: "
					+ vmList.get(i).getStorageCapacity());

			myVMList.add(vmList.get(i));
		}

	}

	public String resourceMonitor() {
		getCloudandVMList();
		for (int i = 0; i < myCloudList.size(); i++) {
			System.out.println(myCloudList.size());
			myVMList.addAll(myCloudList.get(i).getVmList());
		}

		System.out.println("---" + myVMList.size());
		return SUCCESS;
	}

	@SuppressWarnings("deprecation")
	public String saveConfig() {
		// productDAO.updateQty(getOrderId());
		Map session = ActionContext.getContext().getSession();
		session.put("myReq", myReq);
		System.out.println(myReq.getUserId() + "----" + myReq.getRegion()
				+ "----" + myReq.getCpu() + "----" + myReq.getAlgo() + "----"
				+ myReq.getRam() + "----" + myReq.getStorage() + "----");
		inputStream = new StringBufferInputStream("Y");
		return SUCCESS;
	}

	@SuppressWarnings("deprecation")
	public String runSimulation() throws Exception {
		// productDAO.updateQty(getOrderId());
		Map session = ActionContext.getContext().getSession();
		myReq = (Request) session.get("myReq");

		String cpu = myReq.getCpu();
		String ram = myReq.getRam();
		String storage = myReq.getStorage();
		String algo = myReq.getAlgo();
		String requests = myReq.getNumberOfRequest();
		String region = myReq.getRegion();
		System.out.println(region + "   " + requests + "  " + algo + "   "
				+ cpu);
		long startTime = new Date().getTime();
		System.out.println("time in milliseconds" + startTime);
		for (int i = 0; i < Integer.parseInt(requests); i++) {
			loadBalancer(algo, cpu, ram, storage, region);
		}
		long endTime = new Date().getTime();
		System.out.println("time in milliseconds" + endTime);
		long turnAroundTime = endTime - startTime;
		myReq.setTurnAroundTime(String.valueOf(turnAroundTime));

		setMyReq(myReq);

		utilization.setUser_id(myReq.getUserId());
		utilization.setExec_time(myReq.getTurnAroundTime());
		utilization.setCpu(myReq.getCpu());
		utilization.setRam(myReq.getRam());
		utilization.setStorage(myReq.getStorage());
		utilization.setNo_of_req(myReq.getNumberOfRequest());
		utilization.setAlgorithm(myReq.getAlgo());

		DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		utilization.setRequestDateTime(dateformat.format(date));
		int sumOfResource = Integer.parseInt(myReq.getCpu())
				+ Integer.parseInt(myReq.getRam())
				+ Integer.parseInt(myReq.getStorage());
		utilization.setSumOfResource(String.valueOf(sumOfResource));
		utilization.setHoursRequired(myReq.getHoursRequired());
		utilizationDAO.saveUtilization(utilization);
		session.put("myReq", myReq);
		inputStream = new StringBufferInputStream("Y");
		return SUCCESS;
	}

	@SuppressWarnings("deprecation")
	public String getComparisionChart() throws Exception {
		// productDAO.updateQty(getOrderId());
		Map session = ActionContext.getContext().getSession();
		myReq = (Request) session.get("myReq");
		List<Utilization> listCompare = utilizationDAO.getComparision();
		String data = "[";
		for (int i = 0; i < listCompare.size(); i++) {
			if (i == listCompare.size() - 1) {
				if (listCompare.get(i).getAlgorithm().equals("ant")) {
					data += "{'ant':'" + listCompare.get(i).getExec_time()
							+ "'}]";
				} else if (listCompare.get(i).getAlgorithm().equals("bee")) {
					data += "{'bee':'" + listCompare.get(i).getExec_time()
							+ "'}]";
				} else if (listCompare.get(i).getAlgorithm().equals("loc")) {
					data += "{'loc':'" + listCompare.get(i).getExec_time()
							+ "'}]";
				} else {
					data += "{'pso':'" + listCompare.get(i).getExec_time()
							+ "'}]";
				}
			} else {
				if (listCompare.get(i).getAlgorithm().equals("ant")) {
					data += "{'ant':'" + listCompare.get(i).getExec_time()
							+ "'},";
				} else if (listCompare.get(i).getAlgorithm().equals("bee")) {
					data += "{'bee':'" + listCompare.get(i).getExec_time()
							+ "'},";
				} else if (listCompare.get(i).getAlgorithm().equals("loc")) {
					data += "{'loc':'" + listCompare.get(i).getExec_time()
							+ "'},";
				} else {
					data += "{'pso':'" + listCompare.get(i).getExec_time()
							+ "'},";
				}
			}

		}

		inputStream = new StringBufferInputStream(data);
		return SUCCESS;
	}

	@SuppressWarnings("deprecation")
	public String getBillingChart() throws Exception {
		// productDAO.updateQty(getOrderId());
		Map session = ActionContext.getContext().getSession();
		myReq = (Request) session.get("myReq");
		List<BillingInfo> allBillingInfo = utilizationDAO.getAll();
		String data = "[";
		for (int i = 0; i < allBillingInfo.size(); i++) {
			if (i == allBillingInfo.size() - 1) {
				data += "{'name':'" + allBillingInfo.get(i).getUser_name()
						+ "','bill':'" + allBillingInfo.get(i).getBill_amount()
						+ "'}]";
			} else {
				data += "{'name':'" + allBillingInfo.get(i).getUser_name()
						+ "','bill':'" + allBillingInfo.get(i).getBill_amount()
						+ "'},";
			}
		}

		inputStream = new StringBufferInputStream(data);
		return SUCCESS;
	}

	public void honeyBeeAlgo(String cpu, String ram, String storage,

	List<Cloud> myCloudList) throws Exception {

		int newCpuCapacity = 0;
		int newRamCapacity = 0;
		int newStorageCapacity = 0;

		VirtualMachine currentVM = selectVMHonyeBee(cpu, ram, storage,
				myCloudList);

		if (currentVM != null) {
			newCpuCapacity = currentVM.getCpuCapacity() - Integer.parseInt(cpu);
			currentVM.setCpuCapacity(newCpuCapacity);
			newRamCapacity = currentVM.getRamCapacity() - Integer.parseInt(ram);
			currentVM.setRamCapacity(newRamCapacity);
			newStorageCapacity = currentVM.getStorageCapacity()
					- Integer.parseInt(storage);
			currentVM.setStorageCapacity(newStorageCapacity);
			currentVM.setCpuCapacity(newCpuCapacity);

			List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
			if (newCpuCapacity == 0 || newRamCapacity == 0
					|| newStorageCapacity == 0) {
				currentVM.setIsAvailable(0);
				System.out.println("vmid " + currentVM.getVmId() + " full");
			} else {
				System.out.println("Location id--> " + currentVM.getCloudId()
						+ " VM Id --> " + currentVM.getVmId());
			}

			vmDAO.updateVirtualMachine(currentVM);
		} else {
			System.out
					.println("No VM Available in this Cloud. \n Creating New Instance.... Please wait.....\n");
			awsMachine.getCredentials();
			String[] instanceDetails = awsMachine.createInstance(
					IMAGE_ID_AMAZON_LINUX_AMI, "SecurityGroup",
					"Mobile Security Group", "AWSWindows");
			String instanceId = instanceDetails[0];
			String vpcId = instanceDetails[1];
			VirtualMachine vm = new VirtualMachine();
			vm.setVmIdDescription(instanceId);
			vm.setCloudId(vpcId);
			vm.setIsAvailable(1);
			vm.setCpuCapacity(1000);
			vm.setStorageCapacity(1000);
			vm.setRamCapacity(1000);
			vm.setVmState("running");
			vm.setPheromonCount(1);
			vm.setMinDistance(1);
			vm.setVmDistance(1);
			int quality = (int) Math.random() * 10;
			vm.setQuality(quality);
			vmDAO.saveVirtualMachine(vm);
			getCloudandVMList();
			honeyBeeAlgo(cpu, ram, storage, myCloudList);
		}

		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();

		for (int i = 0; i < myCloudList.size(); i++) {
			vmList.addAll(myCloudList.get(i).getVmList());
		}
		myVMList = new ArrayList<VirtualMachine>();
		for (int i = 0; i < vmList.size(); i++) {
			System.out.println("vm id: " + vmList.get(i).getVmId()
					+ "\t cpuCapacity: " + vmList.get(i).getCpuCapacity()
					+ "\t ramCapacity: " + vmList.get(i).getRamCapacity()
					+ "\t storageCapacity: "
					+ vmList.get(i).getStorageCapacity());
			myVMList.add(vmList.get(i));
		}
	}

	public VirtualMachine selectVMAnt(String cpu1, String ram1,
			String storage1, List<Cloud> myCloudList) {

		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		int max = -1;
		int vmId = 0;
		int cpu = Integer.parseInt(cpu1);
		int ram = Integer.parseInt(ram1);
		int storage = Integer.parseInt(storage1);

		VirtualMachine currentVM = null;
		for (int i = 0; i < myCloudList.size(); i++) {
			vmList.addAll(myCloudList.get(i).getVmList());
		}

		for (int i = 0; i < vmList.size(); i++) {
			int pheromon = vmList.get(i).getPheromonCount();

			int cpuCapacity = vmList.get(i).getCpuCapacity();
			int cpuCapacityAvailable = cpuCapacity - cpu;

			int ramCapacity = vmList.get(i).getRamCapacity();
			int ramCapacityAvailable = ramCapacity - ram;

			int storageCapacity = vmList.get(i).getStorageCapacity();
			int storageCapacityAvailable = storageCapacity - storage;

			if (pheromon > max && cpuCapacityAvailable >= 0
					&& ramCapacityAvailable >= 0
					&& storageCapacityAvailable >= 0) {
				max = pheromon;
				vmId = vmList.get(i).getVmId();
				currentVM = vmList.get(i);
			}
		}
		try {
			// VirtualMachine currentVM= vmList.get(vmId-1);
			return currentVM;
		} catch (Exception e) {
			return null;
		}

	}

	public VirtualMachine selectVMHonyeBee(String cpu1, String ram1,
			String storage1, List<Cloud> myCloudList) {
		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		int max = -1;
		int vmId = 0;
		int cpu = Integer.parseInt(cpu1);
		int ram = Integer.parseInt(ram1);
		int storage = Integer.parseInt(storage1);

		VirtualMachine currentVM = null;
		for (int i = 0; i < myCloudList.size(); i++) {
			vmList.addAll(myCloudList.get(i).getVmList());
		}
		int tempQty = 0, tempVMId = 0;
		for (int i = 0; i < vmList.size(); i++) {
			int quality = vmList.get(i).getQuality();
			int cpuCapacity = vmList.get(i).getCpuCapacity();
			int ramCapacity = vmList.get(i).getRamCapacity();
			int storageCapacity = vmList.get(i).getStorageCapacity();

			if (quality >= tempQty && cpuCapacity >= cpu && ramCapacity >= ram
					&& storageCapacity >= storage) {
				tempQty = quality;

				// int capacity = vmList.get(i).getCpuCapacity();
				// int available = capacity-Integer.parseInt(resources);

				int cpuCapacityAvailable = cpuCapacity - cpu;

				int ramCapacityAvailable = ramCapacity - ram;

				int storageCapacityAvailable = storageCapacity - storage;

				if (cpuCapacityAvailable >= 0 && ramCapacityAvailable >= 0
						&& storageCapacityAvailable >= 0) {
					tempVMId = vmList.get(i).getVmId();
					currentVM = vmList.get(i);
				}
			}
		}
		try {
			// VirtualMachine currentVM= vmList.get(tempVMId-1);
			return currentVM;
		} catch (Exception e) {
			return null;
		}

	}

}
