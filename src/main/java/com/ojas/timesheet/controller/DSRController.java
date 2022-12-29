package com.ojas.timesheet.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ojas.timesheet.entity.DSR;
import com.ojas.timesheet.entity.Employee;
import com.ojas.timesheet.repo.DSRRepo;
import com.ojas.timesheet.repo.EmployeeRepo;

@Controller
public class DSRController {

	private static final Logger logger = LogManager.getLogger(DSRController.class);

	@Autowired
	private EmployeeRepo empRepo;
	@Autowired
	private DSRRepo dSRRepo;

	@GetMapping("/dsr")
	public String empDetails(Model model) {

		List<Employee> empDetails = empRepo.findAll();
		model.addAttribute("empDetails", empDetails);
		return "filterData";
	}

	@PostMapping(path = "addDSR", consumes = "application/json", produces = "application/json")
	public @ResponseBody String DSR(@RequestBody String ajaxRequest) {
		JsonObject jobj = new Gson().fromJson(ajaxRequest, JsonObject.class);
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);

		String empName = jobj.get("empName").getAsString();
		String projectName = jobj.get("projectName").getAsString();
		String dsrDate = jobj.get("dsrDate").getAsString();
		String hrs = jobj.get("hrs").getAsString();
		String taskDescription = jobj.get("taskDescription").getAsString();
		logger.info("ajax req is  ---" + ajaxRequest);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date sDate = null;
		try {
			sDate = sdf.parse(dsrDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		com.ojas.timesheet.entity.DSR dsr = new com.ojas.timesheet.entity.DSR();
		dsr.setEmpName(empName);
		dsr.setProject(projectName);
		dsr.setHrsWorked(hrs);
		dsr.setDSRReport(taskDescription);
		dsr.setDSRDate(sDate);
		dsr.setSubmitDate(date);
		dsr.setVisibility(true);

		com.ojas.timesheet.entity.DSR save = dSRRepo.save(dsr);
		Gson gson = new GsonBuilder().create();
		String jsonString = gson.toJson(dsr);
		logger.info("jsonString is ---" + jsonString);
		return jsonString;
	}

	@GetMapping("/fetchdsr")
	public Object featchTechDetails() {
		List<com.ojas.timesheet.entity.DSR> tech = dSRRepo.findAll();
		Gson gson = new GsonBuilder().create();
		String jsonString = gson.toJson(tech);
		System.out.println(jsonString);
		return new ResponseEntity<Object>(jsonString, HttpStatus.OK);
	}

	@GetMapping("/getName/{empName}")
	@ResponseBody
	public List<DSR> findByName(@PathVariable("empName") String empName) {
		List<DSR> emp = dSRRepo.getName(empName);;
		return emp;
	}

}
