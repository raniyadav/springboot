package com.ojas.timesheet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.ojas.timesheet.entity.DSR;
@Repository
@EnableJpaRepositories
public interface DSRRepo extends JpaRepository<DSR, Long> {

	@Query(value = "select d from dsr d where d.project = ?1 and year(d.dsr_date) = ?2 and month(d.dsr_date) = ?3", nativeQuery = true)
	public List<DSR> findByProjectAndMonth(String projectName, String year, String month);
   
	
	 @Query(value="select d from DSR d where d.empName=?1")
	public List<DSR> getName(String empName);
	 
	 @Query(value="select * from dsr d where d.project = ?1 and d.emp_name=?2 and year(d.dsr_date) = ?3 and month(d.dsr_date) = ?4 order by d.dsr_date asc",nativeQuery = true)
	 public List<DSR> fetchTechDetails(String projectName,String ename, String year, String month);

	 @Query(value="select distinct(d.emp_name) from dsr d where d.project = ?1 and year(d.dsr_date) = ?2 and month(d.dsr_date) = ?3 order by emp_name",nativeQuery = true)
	 public List<String> fetchEmpNameByProject(String projectName, String year, String month);
	
	 @Query(value="select distinct(d.emp_name) from dsr d where d.project = ?1 and year(d.dsr_date) = ?2 and month(d.dsr_date) = ?3 order by emp_name",nativeQuery = true)
	 public List<String> fetchEmployeeProjectSummaryDetails(String projectName, String year, String month);
	

}
