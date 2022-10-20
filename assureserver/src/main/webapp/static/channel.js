function getChannelUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/channel";
}

//BUTTON ACTIONS
function addChannel(event){
	//Set the values to update
	var $form = $("#channel-form");
	var json = toJson($form);
	var url = getChannelUrl() + "/create";
    console.log(json)
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   console.log(json.type)
	   $.notify("New Channel Added", "success");
	   		getChannelList();
	   },
	   error: handleAjaxError
	});
	return false;
}

function getChannelList(){
	var url = getChannelUrl() + "/viewAll";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayChannelList(data);
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayChannelList(data){
	var $tbody = $('#channel-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button class = "btn btn-outline-secondary btn-sm" onclick="displayEditChannel(' + e.id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+ '<td>'  + e.invoiceType + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}


//INITIALIZATION CODE
function init(){
	$('#add-channel').click(addChannel);
    $('#channelFile').on('change', updateFileName)
}

function highLight(){
highlightItem("channels")
}

$(document).ready(init);
$(document).ready(highLight);
$(document).ready(getChannelList);


