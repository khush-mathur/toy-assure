function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/channel-page";
}



var fileData = [];
var logsData = [];
var processCount = 0;
//BUTTON ACTIONS
function addOrder(){
	//Set the values to update
	var url =  getOrderUrl() + "/order/new-order";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(response) {
	   console.log(response)
	        getOrderList();
	        goToCart(response.id);
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateOrder(event){
	$('#edit-order-modal').modal('toggle');
	//Get the ID
	var id = $("#order-view-form input[name=id]").val();
	var url = getOrderUrl() + "/update/" + id;

	//Set the values to update
	var $form = $("#order-view-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $.notify("Order Updated", "success");
	   		getOrderList();
	   },
	   error: handleAjaxError
	});
	return false;
}


function getOrderList(){
	var url = getOrderUrl() + "/viewAll";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);
	   },
	   error: handleAjaxError
	});
}

function processData(){
	var file = $('#orderFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){

//	var orderItems = JSON.stringify(fileData);
//    console.log(fileData);
    var $form = $("#order-form");
	var channel = $form.serializeArray()[0]["value"];
	var client = $form.serializeArray()[1]["value"];
	var customer = $form.serializeArray()[2]["value"];
	var channelOrderId = $form.serializeArray()[3]["value"];
	var data = {
	"channelName" : channel,
	"clientName" :client,
	"customerName" : customer,
	"chanOrderId" : channelOrderId,
	"orderItems" : fileData
	}
	var url = getOrderUrl() + "/create";

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: JSON.stringify(data),
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $.notify("Order Created", "success");
	        getOrderList();
	   },
	   error: handleAjaxError
	});

}

function downloadErrors(){
	writeFileData(errorData,'order-errors.tsv');
}

//UI DISPLAY METHODS

function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button  class = "btn btn-outline-info btn-sm" onclick="displayOrderItems(' + e.id + ')">view</button>'
		    buttonHtml += '<button  class = "btn btn-outline-info btn-sm" onclick="generateInvoice(' + e.id + ')">invoice</button>'

		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.clientName + '</td>'
		+ '<td>' + e.customerName + '</td>'
		+ '<td>' + e.channelName + '</td>'
		+ '<td>' + e.channelOrderId + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayOrderItems(id){
	var url = getOrderUrl() + "/viewOrder/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrder(data);
	   },
	   error: handleAjaxError
	});
}

function generateInvoice(id) {
    window.open(
        getOrderUrl() + '/generate-invoice/' + id,
                   '_blank' // <- This is what makes it open in a new window.
        );
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#orderFile');
	$file.val('');
	$('#orderFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#orderFile');
	var fileName = $file.val();
	$('#orderFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-order-modal').modal('toggle');
}


function displayOrder(data){
	$('#view-order-modal').modal('toggle');
	var $tbody = $('#order-item-table').find('tbody');
    	$tbody.empty();

    for(var i in data){
   		var e = data[i];
   		var row = '<tr>'
   		+ '<td>' + i + '</td>'
   		+ '<td>' + e.client + '</td>'
   		+ '<td>' + e.clSku + '</td>'
   		+ '<td>' + e.orderedQuantity + '</td>'
   		+ '<td>' + e.sellingPricePerUnit + '</td>'
    	+ '</tr>';
        $tbody.append(row);
    }
}


function displayOrder(data){

	$('#view-fullfilled-order-modal').modal('toggle');
	var $tbody = $('#view-order-item-table').find('tbody');
    	$tbody.empty();
    var totalAmount =0;
    for(var i in data){
   		var e = data[i];
   		var amount = e.sellingPricePerUnit*e.orderedQuantity
   		var row = '<tr>'
   		+ '<td>' + i + '</td>'
   		+ '<td>' + e.client + '</td>'
   		+ '<td>' + e.channSku + '</td>'
   		+ '<td>' + e.orderedQuantity + '</td>'
   		+ '<td>' + e.sellingPricePerUnit + '</td>'
   		+ '<td>' + amount + '</td>'
    	+ '</tr>';
    	totalAmount +=amount;
        $tbody.append(row);
    }
    displayTotal(totalAmount);
}

function displayTotal(totalAmount){
    document.getElementById("total").innerHTML = totalAmount.toFixed(2);
}

//INITIALIZATION CODE
function init(){
	$('#add-order').click(addOrder);
	$('#update-order').click(updateOrder);
	$('#refresh-data').click(getOrderList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#orderFile').on('change', updateFileName)
}

function highLight(){
highlightItem("Orders")
}

$(document).ready(init);
$(document).ready(highLight);
$(document).ready(getOrderList);

