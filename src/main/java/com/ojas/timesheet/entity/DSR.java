package com.ojas.timesheet.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DSR")
public class DSR {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SNID")
	private long id;
	
	@Column(name = "EMP_NAME")
	private String empName;
	
	@Column(name = "PROJECT")
	private String project;
	
	@Column(name = "DSR_DATE")
	private Date dSRDate;
	
	@Column(name = "HRS_WORKED")
	private String hrsWorked;
	
	@Column(name = "DSR_REPORT",columnDefinition = "MEDIUMTEXT")
	private String dSRReport;
	
	@Column(name = "SUBMIT_DATE")
	private Date submitDate;
	
	@Column(name = "VISIBILITY")
	private boolean visibility;
	
}
