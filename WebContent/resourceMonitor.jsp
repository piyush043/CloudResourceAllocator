<%@taglib uri="/struts-tags" prefix="s"%>
<s:if test="myVMList.size() > 0">
	<div class="content" align="center">
	<table align="center" cellpadding="8" cellspacing="8" border="1" class="ui-state-default">
		<tr class="even">
			<th class="ui-state-active">VM Id</th>
			
			<th class="ui-state-active">CPU Capacity</th>
			<th class="ui-state-active">RAM Capacity</th>
			<th class="ui-state-active">Storage Capacity</th>
			<th class="ui-state-active">Location Id</th>
			<th class="ui-state-active">State</th>
			
		</tr>
		<s:iterator value="myVMList" var="curStr">
			<tr>
				<td class="ui-state-default"><s:property value="vmIdDescription" /></td>
				<td class="ui-state-default"><s:property value="cpuCapacity" /></td>
			<td class="ui-state-default"><s:property value="ramCapacity" /></td>
			<td class="ui-state-default"><s:property value="storageCapacity" /></td>
				<td class="ui-state-default"><s:property value="cloudId" /></td>
				
				
				    <s:if test="#curStr.vmState == 'running'">  
    					<td class="ui-state-highlight"><s:property value="vmState" /></td>
    				</s:if>
    				<s:else>
    				<td class="ui-state-error"><s:property value="vmState" /></td>
  					</s:else>
  					
			</tr>
		</s:iterator>
	</table>
	</div>
</s:if>
