function getBinSkuUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/inventory";
}


function updateBinSku(event){
	$('#edit-bin-sku-modal').modal('toggle');
	//Get the ID
	var id = $("#bin-sku-edit-form input[name=id]").val();
	var url = getBinSkuUrl() + "/bin-wise/update/" + id;

	//Set the values to update
	var $form = $("#bin-sku-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $.notify("bin-sku Updated", "success");
	   		getBinSkuList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getBinSkuList(){
	var url = getBinSkuUrl() + "/bin-wise/viewAll";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBinSkuList(data);
	   },
	   error: handleAjaxError
	});
}



function processData(){
	var file = $('#bin-skuFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(event){
    var $form = $("#bin-sku-form");
    console.log("jdi")
	var clientId = $form.serializeArray()[0]["value"];
	var json = JSON.stringify(fileData);
	console.log(fileData)
	var url = getBinSkuUrl() + "/bin-wise/upload/" + clientId;
	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getBinSkuList();
	   },
	   error: handleAjaxError
	});

}

function downloadErrors(){
	writeFileData(errorData,'bin-sku-errors.tsv');
}

//UI DISPLAY METHODS

function displayBinSkuList(data){
	var $tbody = $('#bin-sku-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml =  ' <button class = "btn btn-outline-secondary btn-sm" onclick="displayEditBinSku(' + e.id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.binId + '</td>'
		+ '<td>' + e.productName + '</td>'
		+ '<td>' + e.clientName + '</td>'
		+ '<td>'  + e.clientSkuId + '</td>'
		+ '<td>' + e.quantity + '</td>'
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

function displayEditBinSku(id){
	var url = getBinSkuUrl() + "/bin-wise/get/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBinSku(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#bin-skuFile');
	$file.val('');
	$('#bin-skuFileName').html("Choose File");
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
	var $file = $('#bin-skuFile');
	var fileName = $file.val();
	$('#bin-skuFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-bin-sku-modal').modal('toggle');
}

function displayBinSku(data){
	$("#bin-sku-edit-form input[name=binId]").val(data.binId);
	$("#bin-sku-edit-form input[name=productName]").val(data.productName);
	$("#bin-sku-edit-form input[name=quantity]").val(data.quantity);
	$("#bin-sku-edit-form input[name=clientSkuId]").val(data.clientSkuId);
	$("#bin-sku-edit-form input[name=clientName]").val(data.clientName);
	$("#bin-sku-edit-form input[name=globalSkuId]").val(data.globalSkuId);
	$("#bin-sku-edit-form input[name=id]").val(data.id);
	$('#edit-bin-sku-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#update-bin-sku').click(updateBinSku);
	$('#process-data').click(processData);
    $('#bin-skuFile').on('change', updateFileName)
}

function highLight(){
highlightItem("bin-skus")
}

$(document).ready(init);
$(document).ready(highLight);
$(document).ready(getBinSkuList);
$(document).ready(displayClients);

