package com.vaannila.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.DescribeAccountAttributesRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeKeyPairsRequest;
import com.amazonaws.services.ec2.model.DescribeKeyPairsResult;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.services.ec2.model.DescribeSubnetsRequest;
import com.amazonaws.services.ec2.model.DescribeSubnetsResult;
import com.amazonaws.services.ec2.model.DescribeVpcsRequest;
import com.amazonaws.services.ec2.model.DescribeVpcsResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.InstanceStatus;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.KeyPairInfo;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.Subnet;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.Vpc;
import com.amazonaws.services.opsworks.model.StartInstanceRequest;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONObject;
import com.amazonaws.services.cloudwatch.*;
import com.amazonaws.services.cloudwatch.model.*;

public class AmazonMachine {
	private static AmazonEC2Client amazonEC2Client;
	private static File credentialsFile = new File("F://Project//JavaProjects//AwsCloudAlgorithm//src//credentials.properties");
	private static List<SecurityGroup> securityGroupList = new ArrayList<SecurityGroup>();
	//private static List<Vpc> vpcList = new ArrayList<Vpc>();
	private static List<Subnet> subnetList = new ArrayList<Subnet>();
	private static List<KeyPairInfo> keyPairList = new ArrayList<KeyPairInfo>();
	//private static List<Instance> instacesList = new ArrayList<Instance>();
	private static Set<Instance> instances = new HashSet<Instance>();
	
	private static String INSTANCE_TYPE_MICRO = "t2.micro";
	
	
	private static String IMAGE_ID_AMAZON_LINUX_AMI = "ami-b5a7ea85";
	private static String IMAGE_ID_WINDOWS_SERVER_2012_RTM = "ami-51f0bc61";
	private static String IMAGE_ID_CUSTOM_WINDOWS = "ami-896f19b9"; 
	private static String INSTANCE_TYPE_SMALL = "m1.small";
	private static String INSTANCE_TYPE_LARGE = "g2.2xlarge"; 
	
	public void getCredentials() throws Exception
	{
		//Amazon credentials
		AWSCredentials credentials = new PropertiesCredentials(credentialsFile);
		
		amazonEC2Client = new AmazonEC2Client(credentials);
		
		//Amazon region endpoint
		amazonEC2Client.setEndpoint("ec2.us-west-2.amazonaws.com");
	}
	
	public String[] createInstance(String imageID, String securityGroupName, String securityGroupDescritpion, String keyPairName )
	{
		
		//Setting up the security group
		boolean isSecurityGroupExist = amazonEC2Client.describeSecurityGroups().getSecurityGroups().contains(securityGroupName);
		
		System.out.println(isSecurityGroupExist);
		if (isSecurityGroupExist)
		{
			CreateSecurityGroupRequest createSecurityGroupRequest = new CreateSecurityGroupRequest();
			createSecurityGroupRequest.withGroupName(securityGroupName).withDescription(securityGroupDescritpion);
			amazonEC2Client.createSecurityGroup(createSecurityGroupRequest);
		//Setting permission using IP range and ports
		IpPermission ipPermission = new IpPermission();
		//Permission for TCP port 
		ipPermission.withIpRanges("111.111.111.111/32", "150.150.150.150/32").withIpProtocol("tcp").withFromPort(22).withToPort(22);
		//Authorizing security groups
		AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest = new AuthorizeSecurityGroupIngressRequest();
		authorizeSecurityGroupIngressRequest.withGroupName(securityGroupName).withIpPermissions(ipPermission);
		//Linking the EC2 client with the security group created
		amazonEC2Client.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
		}
		
		/*Creating a new key pair
		//amazonEC2Client.describeKeyPairs().getKeyPairs().
		CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest();
		//Setting a name for the key pair
		createKeyPairRequest.withKeyName(keyPairName);
		//Result object after key pair creation
		CreateKeyPairResult createKeyPairResult = amazonEC2Client.createKeyPair(createKeyPairRequest);
		
		//Getting the key pair
		KeyPair keyPair = new KeyPair();
		keyPair = createKeyPairResult.getKeyPair();
		String privateKey = keyPair.getKeyMaterial();
		*/
		//Running an Amazon EC2 instance
		RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
		//Micro instance - t1.micro (ami-10fd7020)
		//Maximum number gives the number of instances to be created
		runInstancesRequest.withImageId(imageID).withInstanceType(INSTANCE_TYPE_MICRO).withMinCount(1).withMaxCount(1)
		.withKeyName(keyPairName).withSecurityGroups(securityGroupName);
		//Running the instance
		RunInstancesResult runInstancesResult = amazonEC2Client.runInstances(runInstancesRequest);
		
		String[] instanceDetails = new String[2];
		
		//String instance = runInstancesResult.getReservation().getInstances().get(0).toString();
		String instanceID = runInstancesResult.getReservation().getInstances().get(0).getInstanceId().toLowerCase();
		String vpcID = runInstancesResult.getReservation().getInstances().get(0).getVpcId().toLowerCase();
		instanceDetails[0] = instanceID;
		instanceDetails[1] = vpcID;
		
		System.out.print("Instance Created with Instance ID: " + instanceID);
		
		return instanceDetails;
	}
	
	/*public static String startInstance1(String instanceID)
	{
		
		RunInstancesRequest runInstancesRequest = new RunInstancesRequest();

		runInstancesRequest.withImageId(imageID).withInstanceType(instanceType).withMinCount(noOfInstances).withMaxCount(noOfInstances)
		.withKeyName(keyPairList.get(0).getKeyName().trim()).withSecurityGroupIds(securityGroupList.get(1).getGroupId().trim());
	
		//Running the instance
		RunInstancesResult runResult = amazonEC2Client.runInstances(runInstancesRequest);
		
		//String instanceID = runResult.getReservation().getInstances().get(0).getInstanceId().toString();
		
		System.out.print("Instance started with Instance ID: " + instanceID);
		
		System.out.println("Instance ID: " + instanceID);
		
		return instanceID;
	}*/
	
	public Instance getInstanceWithInstanceID(String instanceID)
	{
		DescribeInstancesRequest instanceDescriptionRequest = new DescribeInstancesRequest().withInstanceIds(instanceID);
		DescribeInstancesResult instanceDescriptionResult = amazonEC2Client.describeInstances(instanceDescriptionRequest);
		
		//Instance ins = instanceDescriptionResult.getReservations().get(0).getInstances().get(0);
		Reservation reservation = instanceDescriptionResult.getReservations().get(0);
		
		Instance instance = reservation.getInstances().get(0);
		//String currentState = ins.getState().getName();
		
		//List<InstanceStatus> state = instanceDescriptionResult.getInstanceStatuses();
		//String currentState = state.get(0).getInstanceState().getName();
		//System.out.println(currentState);
		return instance;
	}
	
	public void startInstance(String instanceID)
	{
		try{
			Instance instance = getInstanceWithInstanceID(instanceID);
			String currentState = instance.getState().getName().trim();
			//System.out.println("current state : " + currentState);
			if(!(currentState.equalsIgnoreCase("running"))) // && currentState!="pending"))
			{
				List<String> startList = new LinkedList<String>();
				startList.add(instanceID);
				StartInstancesRequest startInstancesRequest = new StartInstancesRequest(startList);
				amazonEC2Client.startInstances(startInstancesRequest);
				
				System.out.print("Instance started with Instance ID: " + instanceID);	
			}
			else
			{
				System.out.print("Instance with Instance ID: " + instanceID + " already in running or pending state.");	
				
			}

		}
		catch (Exception e)
		{
			System.out.println("Exception");
			e.printStackTrace();
		}
	}
	
	public void stopInstance(String instanceID)
	{
		Instance instance = getInstanceWithInstanceID(instanceID);
		String currentState = instance.getState().getName().trim();
		//System.out.println("current state : " + currentState);
		if(!(currentState.equalsIgnoreCase("stopped")))
		{
			List<String> stopList = new LinkedList<String>();
			stopList.add(instanceID);
			StopInstancesRequest stopRequest = new StopInstancesRequest(stopList);
			amazonEC2Client.stopInstances(stopRequest);
			System.out.print("Instance stopped with Instance ID: " + instanceID);	
		}
		else
		{
			System.out.println("Instance with Instance ID: " + instanceID + " already in stopped or stopping state.");
		}
	}
	
	public static void terminateInstance(String instanceID)
	{
		if(!instanceID.contains("i-961e559e") && !instanceID.contains("i-9a247a92"))
		{
			List<String> terminateList = new LinkedList<String>();
			terminateList.add(instanceID);
			TerminateInstancesRequest terminateRequest = new TerminateInstancesRequest(terminateList);
            amazonEC2Client.terminateInstances(terminateRequest);
            System.out.print("Instance terminated with Instance ID: " + instanceID);
    		
		}
		else
		{
			System.out.println("Cannot delete permanent instance!");
		}
	}
	
	public static void getSecurityGroup()
	{
		//Security Group ID
		//DescribeSecurityGroupsRequest securityGroupRequest = new DescribeSecurityGroupsRequest();
				
		DescribeSecurityGroupsResult securityGroupResult = amazonEC2Client.describeSecurityGroups();
		securityGroupList = securityGroupResult.getSecurityGroups();
		for (int i = 0; i < securityGroupList.size(); i++)
		{
			System.out.println("Security Group " + i + ": " + securityGroupList.get(i).getGroupId().toString());
			System.out.println("Security Group VPC " + i + ": " + securityGroupList.get(i).getVpcId().toString());
		}
	}

	public static void getSubnet()
	{
		//Subnet ID
		//DescribeSubnetsRequest subnetRequest = new DescribeSubnetsRequest();
		
		DescribeSubnetsResult subnetResult = amazonEC2Client.describeSubnets();
		subnetList = subnetResult.getSubnets();
		for (int i = 0; i < subnetList.size(); i++)
		{
			System.out.println("Subnet: " + subnetList.get(i).getSubnetId().toString());
		}
	}
	
	public static void getKeyPair()
	{
		//Keypair ID
		//DescribeKeyPairsRequest keyPairsRequest = new DescribeKeyPairsRequest();
		
		DescribeKeyPairsResult keyPairsResult = amazonEC2Client.describeKeyPairs();
		keyPairList = keyPairsResult.getKeyPairs();
		for (int i = 0; i < keyPairList.size(); i++)
		{		
			System.out.println("KeyPair: " + keyPairList.get(i).getKeyName().toString());
		}
	}
	
	public static void collectData()
	{
		getSecurityGroup();
		
		getSubnet();
		
		getKeyPair();
	}
		
	public static void getStatus()
	{
		//Getting all the instance states
		//DescribeInstancesRequest instanceRequest = new DescribeInstancesRequest();
	    DescribeInstancesResult describeInstancesRequest = amazonEC2Client.describeInstances();
	    List<Reservation> reservations = describeInstancesRequest.getReservations();

	    //Add all instances to a Set
	    for (Reservation reservation : reservations) 
	    {
	    	instances.addAll(reservation.getInstances());
	    }
	    
	    System.out.println("Number of instances: " + instances.size());
	    for (Instance ins : instances)
	    {
	        //Instance id
	        String instanceId = ins.getInstanceId();
	        
	        //Instance state
	        InstanceState is = ins.getState();
	        
	        System.out.println(instanceId+" "+is.getName() + 
	        		"\t Image ID: " + ins.getImageId() + 
	        		"\t VPC ID: " + ins.getVpcId() +
	        		"\t Security Groups: " + ins.getSecurityGroups() +
	        		"\t Instance Type: " + ins.getInstanceType()); 
	    }	
	}

	public List<Instance> getInstanceList(){
		DescribeInstancesResult result = amazonEC2Client.describeInstances();
		List<Reservation> reservationList = result.getReservations();
		List<Instance> instanceList = new ArrayList<Instance>();
		for(Reservation reservation : reservationList)
		{
			instanceList.addAll(reservation.getInstances());
		}
		
		/*
		System.out.println("Total Number of Instances: " + instanceList.size());
		for(int i = 0; i< instanceList.size(); i++)
		{
			System.out.println("instance ID: " + instanceList.get(i).getInstanceId());
		}
		*/
		return instanceList;
		
	}
	
}
