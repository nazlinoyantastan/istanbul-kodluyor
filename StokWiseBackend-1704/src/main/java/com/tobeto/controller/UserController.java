package com.tobeto.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.SuccessResponseDTO;
import com.tobeto.dto.signup.SignupRequestDTO;
import com.tobeto.dto.signup.SignupResponseDTO;
import com.tobeto.dto.user.ChangePasswordUserRequestDTO;
import com.tobeto.dto.user.GetAllUsersResponseDTO;
import com.tobeto.dto.user.UserDTO;
import com.tobeto.entities.user.User;
import com.tobeto.service.LoginService;
import com.tobeto.service.TokenService;
import com.tobeto.service.UserService;

@RestController
@RequestMapping("/api/v1/admin")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private LoginService loginService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	@Qualifier("requestMapper")
	private ModelMapper requestMapper;

	@Autowired
	@Qualifier("responseMapper")
	private ModelMapper responseMapper;

	@GetMapping("/user/getAll")
	public ResponseEntity<List<GetAllUsersResponseDTO>> getAllUsers() {
		List<User> users = userService.getAllUser();
		List<GetAllUsersResponseDTO> userDTOs = new ArrayList<>();
		users.forEach(user -> {
			userDTOs.add(responseMapper.map(user, GetAllUsersResponseDTO.class));
		});
		return ResponseEntity.ok(userDTOs);

	}

	@PostMapping("/user/signup")
	public ResponseEntity<SignupResponseDTO> userSignUp(@Validated @RequestBody SignupRequestDTO signupRequestDTO) {

		User user = loginService.userSignUp(signupRequestDTO.getEmail(), signupRequestDTO.getPassword());
		String token = tokenService.createToken(user);
		return ResponseEntity.ok(new SignupResponseDTO(token));
	}

	@PostMapping("/user/update")
	public SuccessResponseDTO userUpdate(@RequestBody UserDTO userDTO) {
		User user = requestMapper.map(userDTO, User.class);
		userService.updateUser(user);
		return new SuccessResponseDTO("User updated!");
	}

	@PostMapping("/user/delete")
	public SuccessResponseDTO deleteUser(@RequestBody UserDTO userDTO) {
		User user = requestMapper.map(userDTO, User.class);
		userService.deleteUser(user);
		return new SuccessResponseDTO("User deleted!");

	}

	@PostMapping("/changePassword")
	public ResponseEntity<SuccessResponseDTO> sifreDegistir(@RequestBody ChangePasswordUserRequestDTO dto,
			Principal principal) {
		boolean result = userService.changePassword(dto.getOldPassword(), dto.getNewPassword(), principal.getName());
		if (result) {
			return ResponseEntity.ok(new SuccessResponseDTO("Password successfully changed."));
		} else {
			return ResponseEntity.internalServerError().build();
		}

	}

}