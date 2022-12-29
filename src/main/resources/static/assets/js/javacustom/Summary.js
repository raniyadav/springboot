$(document).ready(function() {

	var currentYear = new Date().getFullYear();
	var currentMonth = new Date().getMonth();
	var cascadedDropDownMonthId = "#dropDownYearMonth";

	//Adding Last 10 Years to Year Drop Down
	for (var i = currentYear; i > currentYear - 10; i--) {
		$("#dropDownYear1").append('<option value="' + i.toString() + '">' + i.toString() + '</option>');
	}

	//Disabling Month Dropdown in case of invalid Selections.
	$(cascadedDropDownMonthId).prop("disabled", true);

	$("#dropDownYear1").change(function() {

		var currentSelectedValue = $(this).val();

		if (currentSelectedValue == "-1") {
			$(cascadedDropDownMonthId).prop("disabled", true);
		}
		else {
			$(cascadedDropDownMonthId).prop("disabled", false);
			//Get Current Year from Dropdown and Converting to Integer for performing math operations
			var currentSelectedYear = parseInt($(this).val());

			//As Index of Javascript Month is from 0 to 11 therefore totalMonths are 11 NOT 12
			var totalMonths = 11;
			if (currentSelectedYear == currentYear) {
				totalMonths = currentMonth;
			}
			var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
			//Emptying the Month Dropdown to clear our last values
			$(cascadedDropDownMonthId).empty();

			$(cascadedDropDownMonthId).append('<option value="-1">-Month-</option>');

			//Appending Current Valid Months
			for (var month = 0; month <= totalMonths; month++) {
				$(cascadedDropDownMonthId).append('<option value="' + (month + 1) + '">' + monthNames[month] + '</option>');
			}
		}
	});


	$("#dsrSummary").submit(function(e) {
		e.preventDefault();
		var projectName = $("#projectName").val();
		var year = $("#dropDownYear1").val();
		var month = $("#dropDownYearMonth").val();
		//alert("Hello");

		var json = {
			"projectName": projectName,
			"year": year,
			"month": month,
		};

		console.log(json);

		$.ajax({
			url: "/fetchsummary",
			type: "post",
			contentType: "application/json",
			data: JSON.stringify(json),
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
	});
});