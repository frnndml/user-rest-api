package com.user.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.user.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, String> {

	User findByEmail(String name);
	
}
