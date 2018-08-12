package com.user;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.user.model.Profile;
import com.user.model.User;
import com.user.repository.UserRepository;

@Component
public class DataLoader {

	@Autowired
    private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
    @PostConstruct
    public void init() {
        clear();
        load();
    }

    private void clear() {
        userRepository.deleteAll();
    }
    
    private void load() {
    	User user = createUserAdmin();   	
    	userRepository.save(user);
    }

	private User createUserAdmin() {
		User user = new User();
    	user.setName("Admin");
    	user.setEmail("admin@admin.com");
    	user.setPassword(bCryptPasswordEncoder.encode("1234"));
    	user.setPhone("+55 48 9888-8888");
    	user.setAddress("Rua Principal,1");
    	user.setProfile(Profile.ADMIN);
		return user;
	}
}
