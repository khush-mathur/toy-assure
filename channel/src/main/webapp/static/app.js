
//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
//    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

function toJsonArray($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    data = [data]
    var json = JSON.stringify(data);
    return json;
}

function handleAjaxError(response){
//	var response = JSON.parse(response.responseText);
//	alert(response.message);
    $('.notifyjs-corner').empty();
    $.notify(response['responseJSON']['message'], { autoHide: false });
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}


function writeFileData(arr,fileName){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, fileName);
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', fileName);
    tempLink.click(); 
}
function highlightItem(head_name) {
    let elements = document.getElementsByClassName('nav-link');
    Array.from(elements).forEach(element => {
        if (element.text == head_name) {
            element.classList.add("active");
        } else {
            element.classList.remove("active");
        }
    });
}
