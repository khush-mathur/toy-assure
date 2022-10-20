function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/user";
}

//BUTTON ACTIONS
function addUser(event){
	//Set the values to update
	var $form = $("#user-form");
	var json = toJson($form);
	var url = getUserUrl() + "/create";
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
	   $.notify("New User Added", "success");
	   		getUserList();
	   },
	   error: handleAjaxError
	});
	return false;
}

function updateUser(event){
	$('#edit-user-modal').modal('toggle');
	//Get the ID
	var id = $("#user-edit-form input[name=id]").val();
	var url = getUserUrl() + "/update/" + id;

	//Set the values to update
	var $form = $("#user-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	        $.notify("User Updated successfully", "success");
	   		getUserList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getUserList(){
	var url = getUserUrl() + "/viewAll";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayUserList(data);
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayUserList(data){
	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button class = "btn btn-outline-secondary btn-sm" onclick="displayEditUser(' + e.id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+ '<td>'  + e.type + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditUser(id){
	var url = getUserUrl() + "/view/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayUser(data);
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#userFile');
	$file.val('');
	$('#userFileName').html("Choose File");
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
	var $file = $('#userFile');
	var fileName = $file.val();
	$('#userFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-employee-modal').modal('toggle');
}

function displayUser(data){
	$("#user-edit-form input[name=name]").val(data.name);
	$("#user-edit-form select[name=type]").val(data.type);
	$("#user-edit-form input[name=id]").val(data.id);
	$('#edit-user-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-user').click(addUser);
	$('#update-user').click(updateUser);
    $('#userFile').on('change', updateFileName)
}

function highLight(){
highlightItem("users")
}

$(document).ready(init);
$(document).ready(highLight);
$(document).ready(getUserList);


