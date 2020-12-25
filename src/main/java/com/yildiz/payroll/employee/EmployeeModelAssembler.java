package com.yildiz.payroll.employee;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/*
 * This simple interface has one method: toModel(). It is based on 
 * converting a non-model object (Employee) into a model-based object 
 * (EntityModel<Employee>).
 * 
 * To leverage this assembler, you only have to alter the EmployeeController 
 * by injecting the assembler in the constructor.
 * 
 */

@Component
public class EmployeeModelAssembler implements
RepresentationModelAssembler<Employee, EntityModel<Employee>>{

	@Override
	public EntityModel<Employee> toModel(Employee employee) {
	    return EntityModel.of(employee, //
	            linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
	            linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
	      }
	

}
