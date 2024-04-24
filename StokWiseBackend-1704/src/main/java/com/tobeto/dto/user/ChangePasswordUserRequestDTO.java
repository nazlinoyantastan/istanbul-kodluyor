package com.tobeto.dto.user;

import lombok.Data;

@Data
public class ChangePasswordUserRequestDTO {
	private String oldPassword;
	private String newPassword;

}