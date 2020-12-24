package com.yildiz.Payroll.Employee;

public class EmployeeNotFoundException extends RuntimeException {
	
	  EmployeeNotFoundException(Long id) {
		  super("Could not find employee " + id);
	  }
}
