<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="jquery-ui-1.11.1/themes/start/jquery-ui.css" rel="stylesheet"  type='text/css'>
<script src="jquery-ui-1.11.1/external/jquery/jquery.js"></script>
<script src="jquery-ui-1.11.1/jquery-ui.js"></script>
<title>Load Balancer</title>
<link href='http://fonts.googleapis.com/css?family=Lobster' rel='stylesheet' type='text/css'>
</head>
<style>
.myFonts {
  font-family: 'Lobster';
  font-style: normal;
  font-weight: 400;
  src: local('Lobster'), local('Lobster-Regular'), url(http://fonts.gstatic.com/s/lobster/v11/MWVf-Rwh4GLQVBEwbyI61Q.woff) format('woff');
}

</style>
<body>
<center>
<h1 class="myFonts"><font size="7" color="#FF9900">cloud</font><font size="7" color="#00CC00">9</font>.<font size="7" color="#CC99FF">load balancer</font></h2>
</center>
	<form action="loadBalancerMain" method="post">
	<table align="center" cellpadding="10" cellspacing="10" class="ui-state-default">
	<tr>
	<td class="ui-state-active">
		User
	</td>
	<td>
		<select name="userId">
		<option id="1" value="1">Ambika</option>
		<option id="2" value="2">Jinal</option>
		<option id="3" value="3">Piyush</option>
		<option id="4" value="4">Utsav</option>
		</select>
	</td>
	</tr>
	<tr>
	<td class="ui-state-active">
		No. of CPU
	</td>
	<td>
		<input type="text" name="cpu" id="cpu"/>
	</td>
	</tr>
	<tr>
	<td class="ui-state-active">
		RAM
	</td>
	<td>
		<input type="text" name="ram" id="ram"/>
	</td>
	</tr>
	
	<tr>
	<td class="ui-state-active">
		Storage
	</td>
	<td>
		<input type="text" name="storage" id="storage"/>
	</td>
	</tr>
	
	<tr>
	<td class="ui-state-active">
		Algorithm
	</td>
	<td>
	
	<input type="radio" name="algo" value="ant">Ant Colony<br>
	<input type="radio" name="algo" value="bee">Honey-Bee<br>
	<input type="radio" name="algo" value="loc">Location Aware Multiuser<br>
	<input type="radio" name="algo" value="pso">Particle Swarm Optimization
	</td>
	</tr>
	<tr>
	<td class="ui-state-active">
		No of requests
	</td>
	<td>
		<select name="numberOfRequest">
		
		<option id="100" value="100">100</option>
		<option id="200" value="200">200</option>
		<option id="300" value="300">300</option>
		<option id="400" value="400">400</option>
		<option id="500" value="500">500</option>
		<option id="600" value="600">600</option>
		<option id="700" value="700">700</option>
		<option id="800" value="800">800</option>
		<option id="900" value="900">900</option>
		<option id="1000" value="1000">1000</option>
		</select>
	</td>
	</tr>
	<tr>
	<td class="ui-state-active">
	Region
	</td>
	<td>
	<select name="region">
		<option value="10">India</option>
		<option value="20">China</option>
		<option value="30">USA</option>
		<option value="40">Canada</option>
		<option value="50">UK</option>
	</select>
	</td>
	</tr>
	<tr>
	<td colspan="2" align="center">
	<input type="submit" value="Test">
	</td>
	</tr>
	</table>
	
	
	
	<table align="center" cellpadding="10" cellspacing="10">
	<tr>
	<td class="ui-state-active">
	<a href="billingDashboard">View Billing Information</a>
	</td>
	</tr>
	<tr>
	<td>
	<hr>
	</td>
	</tr>
	
	<tr>
	<td class="ui-state-active">
	<a href="resourceMonitor">Resource Monitor</a>
	</td>
	</tr>
	
	</table>
		
	
	
	</form>

</body>
</html>