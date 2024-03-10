package com.tobeto.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tobeto.entity.Member;
import com.tobeto.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public Optional<Member> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Member createUser(Member member) {
		return userRepository.save(member);
	}

}
