package com.yildiz.payroll.employee;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/*
 * @Entity is a JPA annotation to make this object ready for 
 * storage in a JPA-based data store.
 */

@Entity
class Employee {
	
	
	/*
	 * id, name, and role are attributes of our Employee domain object.
	 * id is marked with more JPA annotations to indicate it’s the primary
	 * key and automatically populated by the JPA provider.
	 */
	
	private @Id @GeneratedValue Long id;
	
	//Field name has been replaced by firstName and lastName.
	private String firstName;
	private String lastName;
	private String role;
	
	Employee(){};
	
	/*
	 * a custom constructor is created when we need to create a new 
	 * instance, but don’t yet have an id.
	 */
	
	Employee(String firstName,String lastName, String role){
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// A "virtual" getter for the old name property, getName() is defined. It uses the firstName and lastName fields to produce a value.
	public String getName() {
		return this.firstName+ " "+this.lastName;
	}

	//A "virtual" setter for the old name property is also defined, setName(). It parses an incoming string and stores it into the proper fields.
	public void setName(String name) {
		String[] parts = name.split(" ");
		this.firstName = parts[0];
		this.lastName = parts[1];
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	  @Override
	  public boolean equals(Object o) {

	    if (this == o)
	      return true;
	    if (!(o instanceof Employee))
	      return false;
	    Employee employee = (Employee) o;
	    return Objects.equals(this.id, employee.id) && Objects.equals(this.firstName, employee.firstName)
	        && Objects.equals(this.lastName, employee.lastName) && Objects.equals(this.role, employee.role);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(this.id, this.firstName, this.lastName, this.role);
	  }

	  @Override
	  public String toString() {
	    return "Employee{" + "id=" + this.id + ", firstName='" + this.firstName + '\'' + ", lastName='" + this.lastName
	        + '\'' + ", role='" + this.role + '\'' + '}';
	  }

}
