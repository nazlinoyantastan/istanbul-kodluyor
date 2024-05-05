package com.tobeto.dto.signup;

import java.util.List;

import com.tobeto.dto.RoleDTO;

import lombok.Data;

@Data
public class SignupRequestDTO {
	private String email;
	private String password;
	private List<RoleDTO> roles;
}
