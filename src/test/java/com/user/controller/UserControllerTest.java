package com.user.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.model.User;
import com.user.security.AuthenticationEntryPoint;
import com.user.security.SpringSecurityConfig;
import com.user.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@EnableSpringDataWebSupport
@Import({ SpringSecurityConfig.class, AuthenticationEntryPoint.class })
public class UserControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserService service;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@WithMockUser(authorities = { "ADMIN", "USER" })
	public void testGet() throws Exception {
		User user = new User();
		user.setName("Name");

		List<User> users = Arrays.asList(user);

		PageRequest pageable = PageRequest.of(0, 2, Direction.ASC, "id");
		given(service.findAll(pageable)).willReturn(users);

		mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].name", is(user.getName())));
	}

	@Test
	@WithMockUser
	public void testGetForbidden() throws Exception {
		mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
	}

	@Test
	public void testGetUnauthorized() throws Exception {
		mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testPost() throws Exception {
		User user = new User();
		user.setName("Name");

		String json = mapper.writeValueAsString(user);
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void testPostForbidden() throws Exception {
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testPut() throws Exception {
		User user = new User();
		user.setId("1");
		user.setName("Name");
		
		String json = mapper.writeValueAsString(user);
		given(service.exists(user.getId())).willReturn(true);

		mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testPutNonExistentUser() throws Exception {
		User user = new User();
		user.setId("1");
		user.setName("Name");
		
		String json = mapper.writeValueAsString(user);
		mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void testPutForbidden() throws Exception {
		mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testDelete() throws Exception {
		String id = "1";
		
		given(service.exists(id)).willReturn(true);

		mvc.perform(delete("/users/{id}", id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testDeleteNonExistentUser() throws Exception {
		String id = "1";

		mvc.perform(delete("/users/{id}", id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void testDeleteForbidden() throws Exception {
		String id = "1";
		
		mvc.perform(delete("/users/{id}", id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
	}
}
