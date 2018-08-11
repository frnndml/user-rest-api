package com.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.user.model.User;
import com.user.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService service;
	
	@GetMapping
	public ResponseEntity<Object> get(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "2") int limit) {
		return new ResponseEntity<>(service.findAll(page, limit), HttpStatus.OK);
	}

	@GetMapping("{id}")
	public ResponseEntity<Object> getById(@PathVariable("id") final String id) {
		if(service.findById(id).isPresent()) {		
			return new ResponseEntity<>(service.findById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<Object>("Object not found", HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping
	public ResponseEntity<Object> post(@RequestBody User user) {
		return new ResponseEntity<>(service.save(user), HttpStatus.OK);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Object> delete(@PathVariable("id") final String id) {
		if(service.exists(id)) {
			service.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<Object>("Object not found", HttpStatus.BAD_REQUEST);
	}
}
