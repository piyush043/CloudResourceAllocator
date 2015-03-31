<!DOCTYPE HTML>
<html>
<head>
<link href="jquery-ui-1.11.1/themes/start/jquery-ui.css" rel="stylesheet"  type='text/css'>
<script src="jquery-ui-1.11.1/external/jquery/jquery.js"></script>
<script src="jquery-ui-1.11.1/jquery-ui.js"></script>

  <script type="text/javascript">
  $( document ).ready(function() {
	  $.ajax({
			type: "POST",
	        url: "getComparisionChart",
	        dataType: "text",
	        cache: false,
	        timeout: 500000,
	        success: function(data) {
	        	
	        	var obj = jQuery.parseJSON(data.replace(/'/g, '"'));
	        	
	        	var antTime = parseInt(obj[0].ant) ;
	        	var beeTime = parseInt(obj[1].bee) ;
	        	var locTime = parseInt(obj[2].loc) ;
	        	var psoTime = parseInt(obj[3].pso) ;
	        	var chart = new CanvasJS.Chart("chartContainer", {

	        	      title:{
	        	        text: "Performance Comparision"              
	        	      },
	        	      data: [//array of dataSeries              
	        	        { //dataSeries object

	        	         /*** Change type "column" to "bar", "area", "line" or "pie"***/
	        	         type: "column",
	        	         dataPoints: [
	        	                    { label: "Ant", y: antTime },
	        	                    { label: "Bee", y: beeTime },
	        	                    { label: "Pso", y: psoTime },                                    
	        	                    { label: "Loc", y: locTime }]
	        	       }
	        	       ]
	        	     });

	        	    chart.render();
//	        	alert(data);
	        	//$("#dialog-monitor").html(data);
	        	//$( "#dialog-bill" ).dialog( "close" );
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
	        	//alert("error");
	        	//$( "#dialog-monitor" ).dialog( "close" );
	        }
	    });

	});
 
  
 
  </script>

  <script type="text/javascript" src="chart/canvasjs.min.js"></script>
</head>
<body>
  <div id="chartContainer" style="height: 300px; width: 100%;">
  </div>
  
  
</body>
</html>



  