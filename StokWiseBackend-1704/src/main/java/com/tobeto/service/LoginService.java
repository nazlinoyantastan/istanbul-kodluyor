package com.tobeto.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tobeto.dto.RoleDTO;
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

	@Autowired
	private PasswordEncoder passwordEncoder;

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
		user.setPassword(passwordEncoder.encode(password));// password encrypt

		user.setRoles(userRoles);

		return userRepository.save(user);
	}

	public String adminSignUp(String email, String password, List<RoleDTO> roleDTOs) {
		User user = new User();
		// List<Role> userRole = roleRepository.findAll();
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password)); // password encrypt

		List<Role> roles = roleDTOs.stream()
				.map(roleDto -> roleRepository.findByName(roleDto.getName())
						.orElseThrow(() -> new RuntimeException("Role not found: " + roleDto.getName()))) // Rol
																											// bulunamazsa
																											// hata
																											// fırlat
				.collect(Collectors.toList());
		user.setRoles(roles);
		userService.createUser(user);
		return tokenService.createToken(user);
	}
}