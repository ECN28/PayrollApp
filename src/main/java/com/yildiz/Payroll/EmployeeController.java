package com.yildiz.Payroll;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * @RestController indicates that the data returned by each method
 * will be written straight into the response body instead of 
 * rendering a template.
 */

@RestController
public class EmployeeController {
	
	private final EmployeeRepository repository;
	
	//An EmployeeRepository is injected by constructor into the controller.
	EmployeeController(EmployeeRepository repository){
		this.repository = repository;
	}
	
	/*
	 * We have routes for each operation (@GetMapping, @PostMapping,
	 * @PutMapping and @DeleteMapping, corresponding to HTTP GET, POST, PUT,
	 * and DELETE calls
	 */
	
	// Aggregate root
	// tag: :get-aggregate-root[]
	@GetMapping("/employees")
	List<Employee> all(){
		return repository.findAll();
	}
	// end: :get-aggregate-root[]
	
	@PostMapping("/employees")
	Employee newEmployee(@RequestBody Employee newEmployee) {
		return repository.save(newEmployee);
	}
	
	//Single item
	
	@GetMapping("/employees/{id}")
	Employee one(@PathVariable Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(id)); //EmployeeNotFoundException is an exception used to indicate when an employee is looked up but not found.
	}
	
	@PutMapping("/employees/{id}")
	Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
		
		return repository.findById(id)
				.map(employee ->{
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				})
				.orElseGet(() ->{
					newEmployee.setId(id);
					return repository.save(newEmployee);
				});
	}
	
	@DeleteMapping("/employees/{id}")
	void deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
	}
	

}
