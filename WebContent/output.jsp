<%@taglib uri="/struts-tags" prefix="s"%>
<form action="loadBalancerMain">
<table align="center" border="0" width="100%" style="vertical-align: top;">
	<tr>
		<td align="center"><font size="3" class="myFonts" color="#000">Performance Information</font></td>
		
		<td align="center"><font size="3" class="myFonts" color="#000">Resource Information</font></td>		
	</tr>
	<tr>
	<td style="vertical-align: top;">
	<table align="center" cellpadding="10" cellspacing="10" border="1" class="ui-state-default">
			<tr>
				<td class="ui-state-active">
 					Algorithm
				</td>
				<td>
					<s:property value="myReq.algo"/>
				</td>
			</tr>
			<tr>
				<td class="ui-state-active">
	 				CPU
				</td>
				<td>
					<s:property value="myReq.cpu"/>
				</td>
			</tr>
			<tr>
				<td class="ui-state-active">
	 				Memory
				</td>
				<td>
					<s:property value="myReq.ram"/>
				</td>
			</tr>
			<tr>
				<td class="ui-state-active">
	 				Storage
				</td>
				<td>
					<s:property value="myReq.storage"/>
				</td>
			</tr>
			<tr>
				<td class="ui-state-active">
					 Region
				</td>
				<td >
					<s:property value="myReq.region"/>
				</td>
			</tr>
			
			<tr>
				<td class="ui-state-active">
					 Execution Time
				</td>
				<td>
					<B>
						<s:property value="myReq.turnAroundTime"/> ms
					</B>
				</td>
			</tr>
</table>
	
	</td>
	<td style="vertical-align: top;">
	<s:if test="myVMList.size() > 0">
	<div class="content" align="center">
	<table align="center" cellpadding="10" cellspacing="10" border="1" class="ui-state-default">
		<tr class="even">
			<th class="ui-state-default">VM Id</th>
			
			<th class="ui-state-default">CPU Capacity</th>
			<th class="ui-state-default">RAM Capacity</th>
			<th class="ui-state-default">Storage Capacity</th>
			
			<th class="ui-state-default">Location Id</th>
		</tr>
		<s:iterator value="myVMList">
			<tr>
				<td class="ui-state-active"><s:property value="vmIdDescription" /></td>
				<td class="ui-state-active"><s:property value="cpuCapacity" /></td>
			<td class="ui-state-active"><s:property value="ramCapacity" /></td>
			<td class="ui-state-active"><s:property value="storageCapacity" /></td>
				<td class="ui-state-active"><s:property value="cloudId" /></td>
			</tr>
		</s:iterator>
	</table>
	</div>
</s:if>
	
	</td>
	</tr>
</table>

</form>
