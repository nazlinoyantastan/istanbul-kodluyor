package com.tobeto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tobeto.entities.user.Role;
import com.tobeto.repository.user.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

}