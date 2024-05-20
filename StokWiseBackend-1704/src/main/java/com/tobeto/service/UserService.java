package com.tobeto.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tobeto.entities.user.Role;
import com.tobeto.entities.user.User;
import com.tobeto.exception.ServiceException;
import com.tobeto.exception.ServiceException.ERROR_CODES;
import com.tobeto.repository.user.UserRepository;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> getAllUser() {
		List<User> allUsers = userRepository.findAllActive();
		allUsers.forEach(u -> {
			List<Role> roles = userRepository.findRolesByEmail(u.getEmail());
			u.setRoles(roles);
		});
		return allUsers;
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User createUser(User user) {
		User createdUser = userRepository.save(user);
		String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("User created: {} by user: {}", createdUser.getEmail(), authenticatedEmail);
		return createdUser;
	}

	public User updateUser(User user) {
		Optional<User> oUser = userRepository.findByEmail(user.getEmail());
		if (oUser.isPresent()) {
			User dbUser = oUser.get();
			dbUser.setEmail(user.getEmail());
			dbUser.setPassword(passwordEncoder.encode(user.getPassword())); // Parola şifreleme

			//
			List<Role> updatedRoles = user.getRoles();
			dbUser.setRoles(updatedRoles);
			//

			User updatedUser = userRepository.save(dbUser);
			String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
			logger.info("User updated: {} by user: {}", updatedUser.getEmail(), authenticatedEmail);
			return updatedUser;
		} else {
			throw new ServiceException(ERROR_CODES.USER_NOT_FOUND);
		}
	}

//	public void deleteUser(User user) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String authenticatedEmail = authentication.getName();
//		User dUser = userRepository.findByEmail(user.getEmail()).orElseThrow();
//		if (dUser.getEmail().equals(authenticatedEmail)) {
//			throw new RuntimeException("ADMİN KENDİNİ SİLEMEZ");
//		}
//		userRepository.delete(dUser);
//	}

	@Transactional
	public void deleteUser(User user) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authenticatedEmail = authentication.getName();

//		User dUser = userRepository.findByEmailAndIsDeletedFalse(user.getEmail())
//				.orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

		User dUser = userRepository.findByEmail(user.getEmail()).orElseThrow();

		if (dUser.getEmail().equals(authenticatedEmail)) {
			throw new RuntimeException("ADMİN KENDİNİ SİLEMEZ");
		}

		dUser.setDeleted(true);
		dUser.setDeletedAt(LocalDateTime.now());

		userRepository.save(dUser);
		logger.info("User deleted: {} by user: {}", dUser.getEmail(), authenticatedEmail);
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
				String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
				logger.info("Password changed for user: {} by user: {}", email, authenticatedEmail);
				return true;
			}
		}
		return false;
	}

}