package com.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.model.User;
import com.user.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public List<User> findAll(Pageable pageable) {
		Page<User> all = repository.findAll(pageable);
		List<User> users = all.getContent();

		return users;
	}

	public User save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return repository.save(user);
	}

	public User update(User user) {
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByEmail(username);

		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getProfile().name());
		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), true, true, true, true, auth);
	}
}
