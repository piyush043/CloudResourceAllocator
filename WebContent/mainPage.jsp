<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href='http://fonts.googleapis.com/css?family=Lobster'
	rel='stylesheet' type='text/css'>
<title>Resource Allocation and Load balancer</title>
</head>
<style>
p:hover, h1:hover, a:hover {
	color: black;
}

.myFonts {
	font-family: 'Lobster';
	font-style: normal;
	font-weight: 400;
	src: local('Lobster'), local('Lobster-Regular'),
		url(http://fonts.gstatic.com/s/lobster/v11/MWVf-Rwh4GLQVBEwbyI61Q.woff)
		format('woff');
}
</style>
<link href="jquery-ui-1.11.1/themes/start/jquery-ui.css"
	rel="stylesheet" type='text/css'>
<script src="jquery-ui-1.11.1/external/jquery/jquery.js"></script>
<script src="jquery-ui-1.11.1/jquery-ui.js"></script>
<script src="jquery.timer.js"></script>
<body>
	<center>
		<span class="myFonts" style="font-size: 48px;color:#DDD">Resource Allocator</span>
		<table class="myFonts">
			<tr>
				<td>
					<div id="cloud1" class="cloud" align="center">
						<span class='shadow'></span>
						<h1 style="color: #3B8CE3">
							<a onclick="config()">Configure Request</a>
						</h1>
					</div>
				</td>
				<td>
					<div id="cloud2" class="cloud" align="center">
						<span class='shadow'></span>
						<h1 style="color: #3B8CE3">
							<a onclick="startTimer()">Run Simulation</a>
						</h1>
					</div>
				</td>
				<td colspan="2" align="center">
					<div id="cloud4" class="cloud" align="center">
						<span class='shadow'></span>
						<h1 style="color: #3B8CE3">
							<a onclick="compare()">Comparative Analysis</a>
						</h1>
					</div>
				</td>
			</tr>
			</table>
			<table class="myFonts">
			<tr>
				
				<td> 
					<div id="cloud3" class="cloud" align="center">
						<span class='shadow'></span>
						<h1 style="color: #3B8CE3">
							<a onclick="openResourceMonitor()">Resource Monitor</a>
						</h1>
					</div>
				</td>
				<td>
					<div id="cloud4" class="cloud" align="center">
						<span class='shadow'></span>
						<h1 style="color: #3B8CE3">
							<a onclick="openBilling()">Billing Dash-board</a>
						</h1>
					</div>
				</td>
			</tr>
		</table>
	</center>
	<div id="dialog-wait" title="Please wait">
		<div class="progress-label">wait...</div>
		<div id="progressbar"></div>
	</div>

	<div id="dialog-compare" title="Comparative Results">
		<jsp:include page="/graphDemo.jsp" />
	</div>

	<div id="dialog-output" title="Execution Result"></div>
	<div id="dialog-bill" title="Billing Dashboard"></div>
	<div id="dialog-monitor" title="Resource Monitor"></div>
	<div id="dialog-form" title="Configure New Request"
		style="font-size: small;">
		<form action="loadBalancerMain" method="post">
			<table align="center" cellpadding="10" cellspacing="10"
				class="ui-state-default">
				<tr>
					<td class="ui-state-active">User</td>
					<td><select name="userId" id="userId">
							<option id="1" value="1">Ambika</option>
							<option id="2" value="2">Jinal</option>
							<option id="3" value="3">Piyush</option>
							<option id="4" value="4">Utsav</option>
					</select></td>
				</tr>
				<tr>
					<td class="ui-state-active">Algorithm</td>
					<td><input type="radio" name="algo" id="algo1" value="ant">Ant
						Colony<br> <input type="radio" name="algo" id="algo2"
						value="bee">Honey-Bee<br> <input type="radio"
						name="algo" id="algo3" value="loc">Location Aware
						Multiuser<br> <input type="radio" name="algo" id="algo4"
						value="pso">Particle Swarm Optimization</td>
				</tr>
				<tr>
					<td class="ui-state-active">No of requests</td>
					<td><select name="numberOfRequest" id="requests">
							<option id="10" value="10">10</option>
							<option id="20" value="20">20</option>
							<option id="30" value="30">30</option>
							<option id="40" value="40">40</option>
							<option id="50" value="50">50</option>
							<option id="60" value="60">60</option>
							<option id="70" value="70">70</option>
							<option id="80" value="80">80</option>
							<option id="90" value="90">90</option>
							<option id="100" value="100">100</option>
					</select></td>
				</tr>
				<tr>
					<td class="ui-state-active">Region</td>
					<td><select name="region" id="region">
							<option value="1">India</option>
							<option value="2">China</option>
							<option value="3">USA</option>
							<option value="4">Canada</option>
							<option value="5">UK</option>
					</select></td>
				</tr>
				<tr>
					<td class="ui-state-active">Time Frame</td>
					<td><select name="duration" id="duration">
							<option value="day">Day</option>
							<option value="ten">10 Hours</option>
							<option value="one">1 Hour</option>
					</select></td>
				</tr>

				<tr>
					<td class="ui-state-active">Hours Required</td>
					<td><input type="text" name="hoursRequired" id="hoursRequired"
						style="width: 80%" /></td>
				</tr>
				<tr>
				<tr>
					<td colspan="2">
						<table>
							<tr>
								<td colspan="3">Per Request:</td>
							</tr>
							<tr class="ui-state-active">
								<td>CPU(Units)</td>
								<td>Memory(GB)</td>
								<td>Storage(GB)</td>
							</tr>
							<tr>
								<td><input type="text" name="cpu" id="cpu"
									style="width: 97%" /></td>
								<td><input type="text" name="ram" id="ram"
									style="width: 97%" /></td>
								<td><input type="text" name="storage" id="storage"
									style="width: 97%" /></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button"
						value="Save" id="saveBtn" onclick="saveParams();"></td>
				</tr>
			</table>
		</form>
	</div>
	<center>
	<br/>
	<footer style="color:white;font-size:12px">Copyright: CmpE-281-01 - Team 10</footer>
	</center>
</body>
</html>
<style>
body {
	background-image: url("images/image.jpg");
	background-repeat: no-repeat;
	background-size: 100%;
	font-size: x-small;
}

.cloud {
	width: 350px;
	height: 100px;
	background: #f2f9fe;
	background: linear-gradient(top, #f2f9fe 5%, d6f0fd 100%);
	background: -webkit-linear-gradient(top, #f2f9fe 5%, d6f0fd 100%);
	background: -moz-linear-gradient(top, #f2f9fe 5%, d6f0fd 100%);
	background: -ms-linear-gradient(top, #f2f9fe 5%, d6f0fd 100%);
	background: -o-linear-gradient(top, #f2f9fe 5%, d6f0fd 100%);
	border-radius: 100px;
	-webkit-border-radius: 100px;
	-moz-border-radius: 100px;
	position: relative;
	margin: 120px auto 20px;
}

.cloud:after, .cloud:before {
	content: '';
	position: absolute;
	background: #f2f9fe;
	z-index: -1
}

.cloud:after {
	width: 100px;
	height: 100px;
	top: -50px;
	left: 50px;
	border-radius: 100px;
	-webkit-border-radius: 100px;
	-moz-border-radius: 100px;
}

.cloud:before {
	width: 180px;
	height: 180px;
	top: -90px;
	right: 50px;
	border-radius: 200px;
	-webkit-border-radius: 200px;
	-moz-border-radius: 200px;
}

.shadow {
	width: 350px;
	position: absolute;
	bottom: -10px;
	background: #000;
	z-index: -1;
	box-shadow: 0 0 25px 8px rgba(0, 0, 0, 0.4);
	-moz-box-shadow: 0 0 25px 8px rgba(0, 0, 0, 0.4);
	-webkit-box-shadow: 0 0 25px 8px rgba(0, 0, 0, 0.4);
	border-radius: 50%;
	border-radius: 50%;
	border-radius: 50%;
}

select {
	width: 200px;
}
</style>
<script type="text/javascript">
	$("#dialog-form").dialog({
		autoOpen : false,
		minHeight : 400,
		width : 600,
		modal : true,
		show : {
			effect : "slide",
			duration : 400
		},
		hide : {
			effect : "slide",
			duration : 400
		}
	});

	$("#dialog-compare").dialog({
		autoOpen : false,
		minHeight : 500,
		width : 600,
		modal : true,
		show : {
			effect : "slide",
			duration : 400
		},
		hide : {
			effect : "slide",
			duration : 400
		}
	});

	function config() {
		$("#dialog-form").dialog("open");
	}
	function compare() {
		$("#dialog-compare").dialog("open");

	}
	function saveParams() {
		var algo = "";
		var cpu = $("#cpu").val();
		var ram = $("#ram").val();
		var storage = $("#storage").val();

		var msg = "";

		if ($("#algo1").is(':checked')) {
			algo = "ant";
		} else if ($("#algo2").is(':checked')) {
			algo = "bee";
		} else if ($("#algo3").is(':checked')) {
			algo = "loc";
		} else if ($("#algo4").is(':checked')) {
			algo = "pso";
		}
		if ($("#userId").val() == '' || $("#region").val() == ''
				|| $("#requests").val() == '' || $("#cpu").val() == ''
				|| $("#ram").val() == '' || $("#storage").val() == ''
				|| !($('input:radio[name=algo]').is(':checked'))
				|| $("#hoursRequired").val() == '') {
			alert("Please fill all details. \n If you don't need resources, enter 0 instead of blank field.");
			return;
		} else {
			if (cpu<0 || cpu>10) {
				msg += "Please enter the valid CPU (0-10)units\n";
			}
			if (ram<0 || ram>50) {
				msg += "Please enter the valid RAM in (0-50)GB\n";
			}
			if (storage<0 || storage>100) {
				msg += "Please enter the valid Storage in (0-100)GB\n";
			}
			if (msg != "") {
				alert(msg);
				return;
			}

		}
		$.ajax({
			type : "POST",
			url : "saveConfig",
			async : false,
			data : {
				userId : $("#userId").val(),
				region : $("#region").val(),
				numberOfRequest : $("#requests").val(),
				algo : $('input:radio[name=algo]:checked').val(),
				cpu : $("#cpu").val(),
				ram : $("#ram").val(),
				storage : $("#storage").val(),
				hoursRequired : $("#hoursRequired").val()
			},
			dataType : "text",
			cache : false,
			timeout : 500000,
			success : function(data) {
				//alert("Successfully Saved Configuration.");
				$("#dialog-form").dialog("close");
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert("error--" + jqXHR.responseText);
			}
		});
	}
	$("#region").selectmenu();
	$("#requests").selectmenu();
	$("#userId").selectmenu();
	$("#duration").selectmenu();
	function runSimulation() {
		$("#dialog-wait").dialog("open");
		$.ajax({
			type : "POST",
			url : "loadBalancerMain",
			dataType : "text",
			cache : false,
			timeout : 500000,
			success : function(data) {
				$("#dialog-wait").dialog("close");
				$("#dialog-output").dialog("open");
				$("#dialog-output").html(data);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				$("#dialog-wait").dialog("close");
				//alert("error" + jqXHR);
				alert("Please configure request properly.")
			}
		});
	}
	$("#progressbar").progressbar({
		value : false
	});
	$("#dialog-wait").dialog({
		autoOpen : false,
		width : 400,
		modal : true,
		show : {
			effect : "slide",
			duration : 0
		},
		hide : {
			effect : "slide",
			duration : 0
		}
	});
	$("#dialog-bill").dialog({
		autoOpen : false,
		minHeight : 500,
		width : 500,
		modal : true,
		show : {
			effect : "slide",
			duration : 0
		},
		hide : {
			effect : "slide",
			duration : 0
		}
	});
	$("#dialog-monitor").dialog({
		autoOpen : false,
		minHeight : 500,
		width : 700,
		modal : true,
		show : {
			effect : "slide",
			duration : 0
		},
		hide : {
			effect : "slide",
			duration : 0
		}
	});
	$("#dialog-output").dialog({
		autoOpen : false,
		minHeight : 500,
		width : 900,
		modal : true,
		show : {
			effect : "slide",
			duration : 0
		},
		hide : {
			effect : "slide",
			duration : 0
		}
	});
	function openBilling() {
		$("#dialog-bill").dialog("open");
		$.ajax({
			type : "POST",
			url : "billingDashboard",
			dataType : "text",
			cache : false,
			timeout : 500000,
			success : function(data) {
				//	alert(data);
				$("#dialog-bill").html(data);
				//$( "#dialog-bill" ).dialog( "close" );
			},
			error : function(jqXHR, textStatus, errorThrown) {
				//alert("error");
				$("#dialog-bill").dialog("close");
			}
		});
	}
	function openResourceMonitor() {
		$("#dialog-monitor").dialog("open");
		$.ajax({
			type : "POST",
			url : "resourceMonitor",
			dataType : "text",
			cache : false,
			timeout : 500000,
			success : function(data) {
				//	alert(data);
				$("#dialog-monitor").html(data);
				//$( "#dialog-bill" ).dialog( "close" );
			},
			error : function(jqXHR, textStatus, errorThrown) {
				//alert("error");
				$("#dialog-monitor").dialog("close");
			}
		});
	}

	var count = 1;
	function startTimer() {
		runSimulation();
		var duration = $("#duration").val();
		var durationInt;
		if (duration == 'day') {
			durationInt = 20;
		} else if (duration == 'ten') {
			durationInt = 10;
		} else {
			durationInt = 1;
		}
		var timer = $.timer(function() {
			var noReq = $("#requests").val();
			var reduced = noReq / durationInt;
			$("#requests").val(reduced);
			runSimulation();
			count++;
		});

		if (duration == 'day') {
			timer.set({
				time : 1000 * 60 * 60 * 20,
				autostart : true
			});
		} else if (duration == 'one') {
			timer.set({
				time : 1000 * 60 * 60 * 1,
				autostart : true
			});
		} else {
			timer.set({
				time : 1000 * 60 * 60 * 10,
				autostart : true
			});
		}

	}
</script>