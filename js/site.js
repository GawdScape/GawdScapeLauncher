var mailURL = "https://projects.coolv1994.com/_E-Mail/Send";
function checkSent(result) {
	if (result == "success") {
		$("#submit").text("E-Mail Sent");
	} else {
		$("#submit").text("Error");
	}
}
function sendEmail(event) {
	event.preventDefault();
	var email = $("#contact").serialize();
	$("#submit").text("Sending");
	$.post(mailURL, email, checkSent);
}
$("#contact").submit(sendEmail);
