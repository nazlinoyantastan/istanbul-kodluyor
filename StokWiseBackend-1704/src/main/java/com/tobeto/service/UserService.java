package com.tobeto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tobeto.entities.user.Role;
import com.tobeto.entities.user.User;
import com.tobeto.exception.ServiceException;
import com.tobeto.exception.ServiceException.ERROR_CODES;
import com.tobeto.repository.user.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> getAllUser() {
		List<User> allUsers = userRepository.findAll();
		allUsers.forEach(u -> {
			List<Role> roles = userRepository.findRolesByEmail(u.getEmail());
//			System.out.println(roles);
			u.setRoles(roles);
		});

		return allUsers;
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}

	public User updateUser(User user) {
		Optional<User> oUser = userRepository.findByEmail(user.getEmail());
		if (oUser.isPresent()) {
			User dbUser = oUser.get();
			dbUser.setEmail(user.getEmail());
			dbUser.setPassword(passwordEncoder.encode(user.getPassword())); // Parola şifreleme

			List<Role> updatedRoles = user.getRoles();
			dbUser.setRoles(updatedRoles);

			return userRepository.save(dbUser);
		} else {
			throw new ServiceException(ERROR_CODES.USER_NOT_FOUND);
		}
	}

	public void deleteUser(User user) {
		Optional<User> dbUser = userRepository.findByEmail(user.getEmail());
		if (dbUser.isPresent()) {
			userRepository.delete(dbUser.get());
		}
	}

	public boolean changePassword(String oldPassword, String newPassword, String email) {
		Optional<User> oUser = userRepository.findByEmail(email);
		if (oUser.isPresent()) {
			User dbUser = oUser.get();
			// Veritabanındaki şifre ile girilen eski şifre karşılaştırılıyor
			if (passwordEncoder.matches(oldPassword, dbUser.getPassword())) {
				// Eski şifre doğruysa, yeni şifreyi şifrele ve güncelle
				dbUser.setPassword(passwordEncoder.encode(newPassword));
				userRepository.save(dbUser);
				return true;

			}
		}
		return false;
	}

//	public List<Role> getUserRolesByEmail(String email) {
//		return userRepository.findRolesByEmail(email);
//	}

}