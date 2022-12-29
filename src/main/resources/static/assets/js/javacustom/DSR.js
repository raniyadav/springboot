$(document).ready(function() {

$.ajax({
		url: "/fetchdsr",
		type: "get",
		contentType: "application/json",
		dataType: 'json',
	}).done(function(result) {
		console.log("------------------result----al---------------------");
		console.log(result);
		Table.clear().draw();
		Table.rows.add(result).draw();
		
	}).fail(function(jqXHR, textStatus, errorThrown) {
		// needs to implement if it fails
		console.log(jqXHR);
		console.log(textStatus);
		console.log(errorThrown);
	})
	Table = $('#getDSR').DataTable({
		columns: [
			{ "data": "dSRDate" },
			{ "data": "hrsWorked" },
			{ "data": "dSRReport" },
		],
	});



	$("#filterDsr").submit(function(e) {
		e.preventDefault();
		var empName = $("#empName").val();
		var projectName = $("#projectName").val();
		var dsrDate = $("#dsrDate").val();
		var hrs = $("#hrs").val();
		var taskDescription = $("#taskDescription").val();
		//alert("Hello");

		var json = {
			"empName": empName,
			"projectName": projectName,
			"dsrDate": dsrDate,
			"hrs": hrs,
			"taskDescription": taskDescription,

		};
		
		console.log(json);

		$.ajax({
			url: "addDSR",
			type: "post",
			contentType: "application/json",
			data: JSON.stringify(json),
			dataType: 'json',
		}).done(function(result) {
			console.log("------------------result-------------------------");
			console.log(result);
			alert("Sucessfully Submitted");
			location.reload();
			Table.clear().draw();
			Table.rows.add(result).draw();

		}).fail(function(jqXHR, textStatus, errorThrown) {
			// needs to implement if it fails
			console.log(jqXHR);
			console.log(textStatus);
			console.log(errorThrown);
		})
	});
});