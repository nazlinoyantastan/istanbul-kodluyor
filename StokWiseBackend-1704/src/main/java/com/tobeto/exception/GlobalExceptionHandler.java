package com.tobeto.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<GlobalExceptionDTO> error(Exception ex) {
		log.error(ex);
		GlobalExceptionDTO dto = new GlobalExceptionDTO();
		dto.setCode(1001);
		dto.setMessage("Hata oluştu");
		return ResponseEntity.internalServerError().body(dto);
	}

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<GlobalExceptionDTO> serviceException(ServiceException ex) {
		log.error(ex);
		GlobalExceptionDTO dto = new GlobalExceptionDTO();
		dto.setCode(ex.getCode());
		dto.setMessage(ex.getMessage());
		return ResponseEntity.internalServerError().body(dto);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}