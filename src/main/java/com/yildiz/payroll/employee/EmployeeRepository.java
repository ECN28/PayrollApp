package com.yildiz.payroll.employee;

import org.springframework.data.jpa.repository.JpaRepository;

/*
 *Spring Data JPA repositories are interfaces with methods supporting creating,
 *reading, updating, and deleting records against a back end data store.  
 */


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
