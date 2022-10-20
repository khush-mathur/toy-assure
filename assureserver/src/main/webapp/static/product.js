function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/product";
}


function updateProduct(event){
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var client = $("#product-edit-form input[name=clientName]").val();
	var url = getProductUrl() + "/update/" + client;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $.notify("Product Updated", "success");
	   		getProductList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getProductList(){
	var url = getProductUrl() + "/viewAll";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var logsData = [];
var processCount = 0;


function processData(){
	var file = $('#productFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(event){
    var $form = $("#product-form");
	var clientId = $form.serializeArray()[0]["value"];
	var json = JSON.stringify(fileData);
	var url = getProductUrl() + "/upload/" + clientId;
	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		writeFileData(response,'product-logs.tsv')
	   		getProductList();
	   },
	   error: function(response){
            writeFileData(response,'product-errors.tsv')
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData,'product-errors.tsv');
}

//UI DISPLAY METHODS

function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml =  ' <button class = "btn btn-outline-secondary btn-sm" onclick="displayEditProduct(' + e.globalSkuId + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.mrp + '</td>'
		+ '<td>' + e.description + '</td>'
		+ '<td>' + e.clientName + '</td>'
		+ '<td>' + e.clientSkuId + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}
function displayClients(){
$.ajax({
        url: $("meta[name=baseUrl]").attr("content") + "/user/viewAllClients",
        type: 'GET',
        success: function (data) {
            window.data = data;
            var list = $('#client_dropdown');
            list.children('option:not(:first)').remove();
            for (var i in data) {
                var item = '<option value="' + data[i].id + '">' +
                    data[i].name + '</option>';
                list.append(item);
            }
        },
        error: handleAjaxError
    });
}

function displayEditProduct(id){
	var url = getProductUrl() + "/view/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=brand]").val(data.brand);
	$("#product-edit-form input[name=clientName]").val(data.clientName);
	$("#product-edit-form input[name=description]").val(data.description);
	$("#product-edit-form input[name=clientSkuId]").val(data.clientSkuId);
	$("#product-edit-form input[name=globalSkuId]").val(data.globalSkuId);
	$('#edit-product-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
//	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName)
}

//function handleAjaxError(response){
//	var response = JSON.parse(response.responseText);
//	alert(response.message);
//}

function highLight(){
highlightItem("Products")
}

$(document).ready(init);
$(document).ready(highLight);
$(document).ready(getProductList);
$(document).ready(displayClients);

