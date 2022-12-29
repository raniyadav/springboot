package com.ojas.timesheet.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.http.HttpHeaders;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ojas.timesheet.entity.DSR;
import com.ojas.timesheet.entity.Employee;
import com.ojas.timesheet.repo.DSRRepo;
import com.ojas.timesheet.repo.EmployeeRepo;

@Controller
public class SummaryController {

	private static final Logger logger = LogManager.getLogger(SummaryController.class);

	@Autowired
	private EmployeeRepo empRepo;
	@Autowired
	private DSRRepo dSRRepo;

	@GetMapping("/summary")
	public String dSRSummary(Model model) {

		List<Employee> empDetails = empRepo.findAll();
		model.addAttribute("empDetails", empDetails);
		return "summary";
	}

	@PostMapping(path = "/fetchsummary", consumes = "application/json", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<StreamingResponseBody> fetchSummary(@RequestBody String ajaxRequest,
			HttpServletResponse response) {
		JsonObject jobj = new Gson().fromJson(ajaxRequest, JsonObject.class);
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		String projectName = jobj.get("projectName").getAsString();
		String year = jobj.get("year").getAsString();
		String month = jobj.get("month").getAsString();
		System.out.println(projectName);
		System.out.println(year);
		System.out.println(month);

		// Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		List<String> empNames = dSRRepo.fetchEmpNameByProject(projectName, year, month);
		for (int i = 0; i < empNames.size(); i++) {
			String ename = empNames.get(i);
			// Creating a blank Excel sheet
			XSSFSheet sheet = workbook.createSheet(ename);
			  
			int rowCount = 0;
			// Creating a new row in the sheet
			Row row0 = sheet.createRow(rowCount);
			rowCount++;
			// Creating a new row in the sheet
			Row row = null;
			System.out.println(ename);
			List<com.ojas.timesheet.entity.DSR> tech = dSRRepo.fetchTechDetails(projectName, ename, year, month);
			for (int k = 0; k < tech.size(); k++) {
				// Creating a new row in the sheet
				row = sheet.createRow(rowCount);

				Date d = tech.get(k).getDSRDate();

				System.out.println(tech.get(k).getEmpName() + "===========" + d.getDay());
				int cellnum = 0;
				Cell cell = row.createCell(cellnum++);

				if (d.getDay() == 0) {
					CellStyle css = workbook.createCellStyle();					
					css.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
					cell.setCellValue("Sunday");
					cell.setCellStyle(css);

				} else if (d.getDay() == 6) {
					cell.setCellValue("Saturday");
				} else {
					cell.setCellValue("WorkingDay");
				}
				cell = row.createCell(cellnum++);
				cell.setCellValue(tech.get(k).getEmpName());
				cell = row.createCell(cellnum++);
				
				CellStyle css = workbook.createCellStyle();
				CreationHelper ch = workbook.getCreationHelper();
				css.setDataFormat(ch.createDataFormat().getFormat("yyyy-mm-dd h:mm:ss"));				
				cell.setCellValue(tech.get(k).getDSRDate());
				cell.setCellStyle(css);
				
				cell = row.createCell(cellnum++);
				cell.setCellValue(tech.get(k).getDSRReport());
				cell = row.createCell(cellnum++);
				cell.setCellValue(tech.get(k).getHrsWorked());

				rowCount++;

			}
		}
		// gen excel report
		// Try block to check for exceptions
		try {
			File f = new File("d:/temp.xlsx");
			// Writing the workbook
			FileOutputStream out = new FileOutputStream(f);
			workbook.write(out);

			// Closing file output connections
			out.close();

			// Console message for successful execution of
			// program
			System.out.println("xlsx written successfully on disk.");

			InputStream in = new FileInputStream(f.getAbsolutePath());
			StreamingResponseBody body = outputStream -> FileCopyUtils.copy(in, outputStream);
			return ResponseEntity.ok().header("Content-Disposition", "attachment;filename=" + f.getName()).body(body);

		} // Catch block to handle exceptions
		catch (Exception e) {

			// Display exceptions along with line number
			// using printStackTrace() method
			e.printStackTrace();
			Gson gson = new GsonBuilder().create();
			String jsonString = gson.toJson("Error");
			System.out.println(jsonString);
			return null;
		}
		/////////////////

	}
	
	 //fetchsummaryBC
	@PostMapping(path = "/fetchsummaryBc", consumes = "application/json", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<StreamingResponseBody> fetchSummaryBc(@RequestBody String ajaxRequest,
			HttpServletResponse response) {
		JsonObject jobj = new Gson().fromJson(ajaxRequest, JsonObject.class);
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		String projectName = jobj.get("projectName").getAsString();
		String year = jobj.get("year").getAsString();
		String month = jobj.get("month").getAsString();
		System.out.println(projectName);
		System.out.println(year);
		System.out.println(month);

		// Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		List<String> empNames = dSRRepo.fetchEmpNameByProject(projectName, year, month);
		for (int i = 0; i < empNames.size(); i++) {
			String ename = empNames.get(i);
			// Creating a blank Excel sheet
			XSSFSheet sheet = workbook.createSheet(ename);
			  
			int rowCount = 0;
			// Creating a new row in the sheet
			Row row0 = sheet.createRow(rowCount);
			rowCount++;
			// Creating a new row in the sheet
			Row row = null;
			System.out.println(ename);
			List<com.ojas.timesheet.entity.DSR> tech = dSRRepo.fetchTechDetails(projectName, ename, year, month);
			for (int k = 0; k < tech.size(); k++) {
				// Creating a new row in the sheet
				row = sheet.createRow(rowCount);

				Date d = tech.get(k).getDSRDate();

				System.out.println(tech.get(k).getEmpName() + "===========" + d.getDay());
				int cellnum = 0;
				Cell cell = row.createCell(cellnum++);

				if (d.getDay() == 0) {
					CellStyle css = workbook.createCellStyle();					
					css.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
					cell.setCellValue("Sunday");
					cell.setCellStyle(css);

				} else if (d.getDay() == 6) {
					cell.setCellValue("Saturday");
				} else {
					cell.setCellValue("WorkingDay");
				}
				cell = row.createCell(cellnum++);
				cell.setCellValue(tech.get(k).getEmpName());
				cell = row.createCell(cellnum++);
				
				CellStyle css = workbook.createCellStyle();
				CreationHelper ch = workbook.getCreationHelper();
				css.setDataFormat(ch.createDataFormat().getFormat("yyyy-mm-dd h:mm:ss"));				
				cell.setCellValue(tech.get(k).getDSRDate());
				cell.setCellStyle(css);
				
				cell = row.createCell(cellnum++);
				cell.setCellValue(tech.get(k).getDSRReport());
				cell = row.createCell(cellnum++);
				cell.setCellValue(tech.get(k).getHrsWorked());

				rowCount++;

			}
		}
		// gen excel report
		// Try block to check for exceptions
		try {
			File f = new File("d:/temp.xlsx");
			// Writing the workbook
			FileOutputStream out = new FileOutputStream(f);
			workbook.write(out);

			// Closing file output connections
			out.close();

			// Console message for successful execution of
			// program
			System.out.println("xlsx written successfully on disk.");

			InputStream in = new FileInputStream(f.getAbsolutePath());
			StreamingResponseBody body = outputStream -> FileCopyUtils.copy(in, outputStream);
			return ResponseEntity.ok().header("Content-Disposition", "attachment;filename=" + f.getName()).body(body);

		} // Catch block to handle exceptions
		catch (Exception e) {

			// Display exceptions along with line number
			// using printStackTrace() method
			e.printStackTrace();
			Gson gson = new GsonBuilder().create();
			String jsonString = gson.toJson("Error");
			System.out.println(jsonString);
			return null;
		}
		/////////////////

	}
}
