function getBinUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/bin";
}

//BUTTON ACTIONS
function addBin(event){
	//Set the values to update
	var $form = $("#bin-form");
	var url = getBinUrl() + "/create/"+$form.serializeArray()[0]['value'];
	$.ajax({
	   url: url,
	   type: 'POST',
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   $.notify("New bin Added", "success");
	   		getBinList();
	   		writeFileData(response,'bins-created.tsv')
	   },
	   error: handleAjaxError
	});
	return false;
}

function getBinList(){
	var url = getBinUrl() + "/viewAll";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBinList(data);
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayBinList(data){
	var $tbody = $('#bin-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button class = "btn btn-outline-secondary btn-sm" onclick="displayEditBin(' + e.id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.binId + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

//INITIALIZATION CODE
function init(){
	$('#add-bin').click(addBin);
}

function highLight(){
highlightItem("bins")
}

$(document).ready(init);
$(document).ready(highLight);
$(document).ready(getBinList);


