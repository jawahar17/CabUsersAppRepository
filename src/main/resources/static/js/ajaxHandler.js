/**
 * 
 */
var jwtToken = sessionStorage.getItem("jwtToken")

//method - requestmethod 
//url - url to which request need to be send
//body - convert body to json before calling the method
//async 

var xhr = new XMLHttpRequest();

function sendAjaxRequest(method, url, body, async) {

	xhr.open(method, url, async);
	xhr.onreadystatechange = processResponse;
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.setRequestHeader("Authorization", jwtToken)
	xhr.send(body);

	function processResponse() {
	
//		while(xhr.readyState!=4) {
//			
//		}
		
		if (xhr.readyState == 4 && xhr.status == 562) {
			window.location.href = "http://localhost:8080/login.html"
		}

		if (xhr.readyState == 4) {

			return xhr;

			//			var obj = {
			//					"readyState":xhr.readyState,
			//					"status":xhr.status,
			//					"responseText":xhr.responseText
			//					};
			//			return obj;
		}

	}

}
//*********************************************************//


//admin contacts button
var adminContBtn = document.getElementById("adminContactsBtn");
adminContBtn.addEventListener("click", getContacts);

var contactsDiv = document.getElementById("adminContacts");

function getContacts() {

	contactsDiv.innerHTML = "";

	var admins = JSON.parse(sessionStorage.getItem("admins"));

	for (let i = 0; i < admins.length; i++) {
		adminName = admins[i].employeeName;
		adminNumber = admins[i].phoneNumber;

		var label = document.createElement("label");
		label.className = "float-start";

//		var anchor = document.createElement("a");
//		anchor.className = "link contact-number";
//		anchor.href = "#";
//		anchor.innerText = adminNumber;

		label.innerText = adminName + " - " + adminNumber;

//		label.appendChild(anchor);
		contactsDiv.appendChild(label);
	}


}
//*********************************************************//
