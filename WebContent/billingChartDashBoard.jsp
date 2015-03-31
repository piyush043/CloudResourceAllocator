<!DOCTYPE HTML>
<html>
<head>
<link href="jquery-ui-1.11.1/themes/start/jquery-ui.css" rel="stylesheet"  type='text/css'>
<script src="jquery-ui-1.11.1/external/jquery/jquery.js"></script>
<script src="jquery-ui-1.11.1/jquery-ui.js"></script>

  <script type="text/javascript">
  
  function openResourceMonitor()
  {
 	 $( "#dialog-monitor" ).dialog( "open" );
 	 $.ajax({
 			type: "POST",
 	        url: "getBillingChart",
 	        dataType: "text",
 	        cache: false,
 	        timeout: 500000,
 	        success: function(data) {
 	        	alert(data.replace(/'/g, '"'));
 	        	var obj = jQuery.parseJSON(data.replace(/'/g, '"'));
 	        	var jinBill,ambiBill,utsiBill,piyuBill;
 	        	for(i=0;i<obj.length;i++)
 	        	{
 	        		if(obj[i].name=="jinal")
 	        			{
 	        			jinBill = parseInt(obj[i].bill);
 	        			}
 	        		if(obj[i].name=="ambika")
	        			{
 	        			ambiBill = parseInt(obj[i].bill);
	        			}
 	        		if(obj[i].name=="utsav")
	        			{
 	        			utsiBill = parseInt(obj[i].bill);
	        			}
 	        		else
	        			{
 	        			piyuBill = parseInt(obj[i].bill);
	        			}
 	        	}
 	        	
 	        	  var chart = new CanvasJS.Chart("chartContainer2",
 	        			    {
 	        			      title:{
 	        			        text: "Billing Information"
 	        			      },
 	        			       data: [
 	        			      {
 	        			         type: "pie",
 	        			       showInLegend: true,
 	        			       dataPoints: [
 	        			       {  y: jinBill, legendText:"Jinal", indexLabel: "Jinal" },
 	        			       {  y: ambiBill, legendText:"Ambika", indexLabel: "Ambika" },
 	        			       {  y: piyuBill, legendText:"Piyush", indexLabel: "Piyush" },
 	        			       {  y: utsiBill, legendText:"Utsav" , indexLabel: "Utsav"}
 	        			       ]
 	        			     }
 	        			     ]
 	        			   });

 	        			    chart.render();

// 	        	alert(data);
 	        	//$("#dialog-monitor").html(data);
 	        	//$( "#dialog-bill" ).dialog( "close" );
 	        },
 	        error: function(jqXHR, textStatus, errorThrown) {
 	        	//alert("error");
 	        	//$( "#dialog-monitor" ).dialog( "close" );
 	        }
 	    });
  }
  
 
  </script>
  <script type="text/javascript">
  window.onload = function () {
    }
  </script>
  
 
  <script type="text/javascript" src="chart/canvasjs.min.js"></script>
</head>
<body>
  <div id="chartContainer2" style="height: 300px; width: 100%;">
   </div>
  <button onclick="openResourceMonitor();">click me!</button>
</body>
</html>



  