function getChannelListingUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/channel-listing";
}


function updateChannelListing(event){
	$('#edit-channel-listing-modal').modal('toggle');
	//Get the ID
	var id = $("#channel-listing-edit-form input[name=id]").val();
	var url = getChannelListingUrl() + "/bin-wise/update/" + id;

	//Set the values to update
	var $form = $("#channel-listing-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        $.notify("channel-listing Updated", "success");
	   		getChannelListingList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getChannelListingList(){
	var url = getChannelListingUrl() + "/viewAll";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayChannelListingList(data);
	   },
	   error: handleAjaxError
	});
}



function processData(){
	var file = $('#channel-listingFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(event){
    var $form = $("#channel-listing-form");
	var client = $form.serializeArray()[0]["value"];
	var channel = $form.serializeArray()[1]["value"];
	var json = JSON.stringify(fileData);
	var url = getChannelListingUrl() + "/upload?clientName=" + client + "&channelName=" + channel;
	//Make ajax call
	$.ajax({
    	url: url,
    	type: 'POST',
    	data: json,
    	headers: {
        	'Content-Type': 'application/json'
        },
    	success: function(response) {
    			writeFileData(response,'listing-logs.tsv')
    			getChannelListingList();
    	},
    	error: function(response){
             writeFileData(response,'listing-errors.tsv')
    	}
    });

}

function downloadErrors(){
	writeFileData(errorData,'channel-listing-errors.tsv');
}

//UI DISPLAY METHODS

function displayChannelListingList(data){
	var $tbody = $('#channel-listing-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml =  ' <button class = "btn btn-outline-secondary btn-sm" onclick="displayEditChannelListing(' + e.id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.channelSkuId + '</td>'
		+ '<td>' + e.channelName + '</td>'
		+ '<td>' + e.clientName + '</td>'
		+ '<td>'  + e.clientSkuId + '</td>'
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
                var item = '<option value="' + data[i].name + '">' +
                    data[i].name + '</option>';
                list.append(item);
            }
        },
        error: handleAjaxError
    });
}
function displayChannels(){
$.ajax({
        url: $("meta[name=baseUrl]").attr("content") + "/channel/viewAll",
        type: 'GET',
        success: function (data) {
            window.data = data;
            var list = $('#channel_dropdown');
            list.children('option:not(:first)').remove();
            for (var i in data) {
                var item = '<option value="' + data[i].name + '">' +
                    data[i].name + '</option>';
                list.append(item);
            }
        },
        error: handleAjaxError
    });
}

function displayEditChannelListing(id){
	var url = getChannelListingUrl() + "/bin-wise/get/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayChannelListing(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#channel-listingFile');
	$file.val('');
	$('#channel-listingFileName').html("Choose File");
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
	var $file = $('#channel-listingFile');
	var fileName = $file.val();
	$('#channel-listingFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-channel-listing-modal').modal('toggle');
}

function displayChannelListing(data){
	$("#channel-listing-edit-form input[name=binId]").val(data.binId);
	$("#channel-listing-edit-form input[name=productName]").val(data.productName);
	$("#channel-listing-edit-form input[name=quantity]").val(data.quantity);
	$("#channel-listing-edit-form input[name=clientSkuId]").val(data.clientSkuId);
	$("#channel-listing-edit-form input[name=clientName]").val(data.clientName);
	$("#channel-listing-edit-form input[name=globalSkuId]").val(data.globalSkuId);
	$("#channel-listing-edit-form input[name=id]").val(data.id);
	$('#edit-channel-listing-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#update-channel-listing').click(updateChannelListing);
	$('#process-data').click(processData);
    $('#channel-listingFile').on('change', updateFileName)
}

function highLight(){
highlightItem("channel-listings")
}

$(document).ready(init);
$(document).ready(highLight);
$(document).ready(getChannelListingList);
$(document).ready(displayClients);
$(document).ready(displayChannels);

