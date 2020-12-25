package com.yildiz.payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @SpringBootApplication is a meta-annotation that pulls in component scanning,
 *  autoconfiguration, and property support. (it will fire up a servlet container
 *  and serve up our service)
 */


@SpringBootApplication
public class PayrollApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayrollApplication.class, args);
	}

}
