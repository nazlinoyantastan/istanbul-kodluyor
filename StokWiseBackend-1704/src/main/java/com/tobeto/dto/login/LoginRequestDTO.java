package com.tobeto.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequestDTO {
	@NotEmpty(message = "Email must not be empty")
	@Email(message = "Email must be valid")
	private String email;
	@NotEmpty(message = "Password must not be empty")
	private String password;
}
