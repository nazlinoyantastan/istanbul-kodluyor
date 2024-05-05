package com.tobeto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tobeto.entities.user.Role;
import com.tobeto.entities.user.User;
import com.tobeto.repository.user.RoleRepository;
import com.tobeto.repository.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class LoginService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private TokenService tokenService;

	@Transactional
	public String login(String email, String password) {
		Optional<User> optionalUser = userService.getUserByEmail(email);
		if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) {
			String token = tokenService.createToken(optionalUser.get());
			return token;
		} else {
			throw new RuntimeException("Login Error");
		}
	}

	@Transactional
	public User userSignUp(String email, String password) {
		User user = new User();
		List<Role> userRoles = roleRepository.findAll();
		userRoles = userRoles.stream()
				.filter(r -> !r.getName().equals("admin") && !r.getName().equals("warehouse-supervisor")).toList();
		user.setEmail(email);
		user.setPassword(password);// password encrypt
// edilecek
		user.setRoles(userRoles);

		return userRepository.save(user);
	}

	public String adminSignUp(String email, String password) {
		User user = new User();
		List<Role> userRole = roleRepository.findAll();
		user.setEmail(email);
		user.setPassword(password);// password encrypt
// edilecek
		user.setRoles(userRole);
		userService.createUser(user);
		return tokenService.createToken(user);
	}
}