package com.yildiz.payroll.order;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class OrderController {
	
	//t injects both an OrderRepository as well as a OrderModelAssembler
	  private final OrderRepository orderRepository;
	  private final OrderModelAssembler assembler;

	  OrderController(OrderRepository orderRepository, OrderModelAssembler assembler) {

	    this.orderRepository = orderRepository;
	    this.assembler = assembler;
	  }
	  
	  //The first two Spring MVC routes handle the aggregate root as well as a single item Order resource request

	  @GetMapping("/orders")
	  CollectionModel<EntityModel<Order>> all() {

	    List<EntityModel<Order>> orders = orderRepository.findAll().stream() //
	        .map(assembler::toModel) //
	        .collect(Collectors.toList());

	    return CollectionModel.of(orders, //
	        linkTo(methodOn(OrderController.class).all()).withSelfRel());
	  }

	  @GetMapping("/orders/{id}")
	  EntityModel<Order> one(@PathVariable Long id) {

	    Order order = orderRepository.findById(id) //
	        .orElseThrow(() -> new OrderNotFoundException(id));

	    return assembler.toModel(order);
	  }

	  //The third Spring MVC route handles creating new orders, by starting them in the IN_PROGRESS state.
	  @PostMapping("/orders")
	  ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order) {

	    order.setStatus(Status.IN_PROGRESS);
	    Order newOrder = orderRepository.save(order);

	    return ResponseEntity //
	        .created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri()) //
	        .body(assembler.toModel(newOrder));
	  }
	  
	  @PutMapping("/orders/{id}/complete")
	  ResponseEntity<?> complete(@PathVariable Long id) {

	    Order order = orderRepository.findById(id) //
	        .orElseThrow(() -> new OrderNotFoundException(id));

	    if (order.getStatus() == Status.IN_PROGRESS) {
	      order.setStatus(Status.COMPLETED);
	      return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
	    }

	    return ResponseEntity //
	        .status(HttpStatus.METHOD_NOT_ALLOWED) //
	        .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
	        .body(Problem.create() //
	            .withTitle("Method not allowed") //
	            .withDetail("You can't complete an order that is in the " + order.getStatus() + " status"));
	  }
	  
	  @DeleteMapping("/orders/{id}/cancel")
	  ResponseEntity<?> cancel(@PathVariable Long id) {

	    Order order = orderRepository.findById(id) //
	        .orElseThrow(() -> new OrderNotFoundException(id));

	    if (order.getStatus() == Status.IN_PROGRESS) {
	      order.setStatus(Status.CANCELLED);
	      return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
	    }

	    return ResponseEntity //
	        .status(HttpStatus.METHOD_NOT_ALLOWED) //
	        .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
	        .body(Problem.create() //
	            .withTitle("Method not allowed") //
	            .withDetail("You can't cancel an order that is in the " + order.getStatus() + " status"));
	  }
	  
	  //All the controller methods return one of Spring HATEOAS’s RepresentationModel subclasses to properly render hypermedia (or a wrapper around such a type).
	}
