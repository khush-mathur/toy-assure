function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/inventory";
}


function getInventoryList(){
	var url = getInventoryUrl() + "/viewAll";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);
	   },
	   error: handleAjaxError
	});
}


//UI DISPLAY METHODS

function displayInventoryList(data){
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button class = "btn btn-outline-secondary btn-sm" onclick="displayEditInventory('+"'" + e.barcode + "'" + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.productName + '</td>'
        + '<td>' + e.clientName + '</td>'
        + '<td>' + e.clientSkuId + '</td>'
        + '<td>' + e.availableQuantity + '</td>'
        + '<td>' + e.allocatedQuantity + '</td>'
        + '<td>' + e.fulfilledQuantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function highLight(){
highlightItem("Inventory")
}

$(document).ready(highLight);
$(document).ready(getInventoryList);

