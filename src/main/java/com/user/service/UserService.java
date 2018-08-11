package com.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.user.model.User;
import com.user.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	public List<User> findAll(int page, int limit) {
//		Sort sort = new Sort(direction, properties);
//		PageRequest request = PageRequest.of(page, limit, sort)
		
		PageRequest request = PageRequest.of(page, limit); 
		Page<User> all = repository.findAll(request);
		List<User> users = all.getContent();
		
		return users;
	}
	
	public User save(User user) {
		return repository.save(user);
	}

	public boolean exists(String id) {
		return repository.existsById(id);
	}
	
	public void delete(String id) {
		repository.deleteById(id);
	}

	public Optional<User> findById(String id) {
		return repository.findById(id);
	}
}
