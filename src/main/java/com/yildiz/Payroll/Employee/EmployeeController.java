package com.yildiz.Payroll.Employee;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
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
	private final EmployeeModelAssembler assembler;
	
	//An EmployeeRepository is injected by constructor into the controller.
	EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler){
		this.repository = repository;
		this.assembler = assembler;
	}
	
	/*
	 * We have routes for each operation (@GetMapping, @PostMapping,
	 * @PutMapping and @DeleteMapping, corresponding to HTTP GET, POST, PUT,
	 * and DELETE calls
	 */
	
	// Aggregate root
	// tag: :get-aggregate-root[]
	@GetMapping("/employees")
	CollectionModel<EntityModel<Employee>> all() {

	  List<EntityModel<Employee>> employees = repository.findAll().stream()
	      .map(assembler::toModel)
	      .collect(Collectors.toList());

	  return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
	}
	// end: :get-aggregate-root[]
	
	@PostMapping("/employees")
	Employee newEmployee(@RequestBody Employee newEmployee) {
		return repository.save(newEmployee);
	}
	
	//Single item
	@GetMapping("/employees/{id}")
	
	/*
	 * The return type of the method has changed from Employee to 
	 * EntityModel<Employee>. EntityModel<T> is a generic container 
	 * from Spring HATEOAS that includes not only the data but a 
	 * collection of links.
	 */
	
	EntityModel<Employee> one(@PathVariable Long id) {

	  Employee employee = repository.findById(id) 
	      .orElseThrow(() -> new EmployeeNotFoundException(id));

	  return assembler.toModel(employee);
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
