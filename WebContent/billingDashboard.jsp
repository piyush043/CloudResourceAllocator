<%@taglib uri="/struts-tags" prefix="s"%>

<center>
	<h1 class="">
		<font size="5" color="#000">Billing Information</font>
		</h2>
</center>

<s:if test="allBillingInfo.size() > 0">
	<div class="content" align="center">
		<table class="userTable" cellpadding="5px">
			<tr class=ui-state-active>
				<th>User Name</th>
				<th>Execution Time</th>
				<th>CPU used</th>
				<th>RAM used</th>
				<th>Storage used</th>
				<th>Bill Amount</th>
			</tr>
			<s:iterator value="allBillingInfo">
				<tr class=ui-state-default>
					<td><s:property value="user_name" /></td>
					<td><s:property value="exec_time" /></td>
					<td><s:property value="cpu" /></td>
					<td><s:property value="ram" /></td>
					<td><s:property value="storage" /></td>
					<td>$ <s:property value="bill_amount" /></td>
				</tr>
			</s:iterator>
		</table>
	</div>
</s:if>
<hr>
<center>
	<button onclick="billingChart();">View Billing DashBoard</button>
</center>
<div id="chartContainer2" style="height: 300px; width: 100%;"></div>

</body>

<script type="text/javascript">
	function billingChart() {

		$.ajax({
			type : "POST",
			url : "getBillingChart",
			dataType : "text",
			cache : false,
			timeout : 500000,
			success : function(data) {

				var obj = jQuery.parseJSON(data.replace(/'/g, '"'));
				var jinBill, ambiBill, utsiBill, piyuBill;
				for (i = 0; i < obj.length; i++) {
					if (obj[i].name == "jinal") {
						jinBill = parseInt(obj[i].bill);
					}
					if (obj[i].name == "ambika") {
						ambiBill = parseInt(obj[i].bill);
					}
					if (obj[i].name == "utsav") {
						utsiBill = parseInt(obj[i].bill);
					} else {
						piyuBill = parseInt(obj[i].bill);
					}
				}

				var chart = new CanvasJS.Chart("chartContainer2", {
					title : {
						text : ""
					},
					data : [ {
						type : "pie",
						showInLegend : true,
						dataPoints : [ {
							y : jinBill,
							legendText : "Jinal",
							indexLabel : "Jinal"
						}, {
							y : ambiBill,
							legendText : "Ambika",
							indexLabel : "Ambika"
						}, {
							y : piyuBill,
							legendText : "Piyush",
							indexLabel : "Piyush"
						}, {
							y : utsiBill,
							legendText : "Utsav",
							indexLabel : "Utsav"
						} ]
					} ]
				});

				chart.render();

				// 	        	alert(data);
				//$("#dialog-monitor").html(data);
				//$( "#dialog-bill" ).dialog( "close" );
			},
			error : function(jqXHR, textStatus, errorThrown) {
				//alert("error");
				//$( "#dialog-monitor" ).dialog( "close" );
			}
		});
	}
</script>



<script type="text/javascript" src="chart/canvasjs.min.js"></script>
