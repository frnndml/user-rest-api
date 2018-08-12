package com.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.model.User;
import com.user.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService service;
	
	@GetMapping
	public ResponseEntity<Object> get(@PageableDefault(page = 0, size = 2, sort = "id", direction = Direction.ASC) Pageable pageable) {
		List<User> findAll = service.findAll(pageable);
		return new ResponseEntity<>(findAll, HttpStatus.OK);
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

	@PutMapping
	public ResponseEntity<Object> put(@RequestBody User user) {
		if(service.exists(user.getId())) {		
			return new ResponseEntity<>(service.update(user), HttpStatus.OK);
		}
		return new ResponseEntity<Object>("Object not found", HttpStatus.BAD_REQUEST);
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