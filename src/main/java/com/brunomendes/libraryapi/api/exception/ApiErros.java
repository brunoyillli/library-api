package com.brunomendes.libraryapi.api.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import com.brunomendes.libraryapi.exception.BusinessException;

public class ApiErros {
	private List<String> errors;
	public ApiErros(BindingResult bindingResult) {
		this.errors = new ArrayList<>();
		bindingResult.getAllErrors().forEach( error -> this.errors.add(error.getDefaultMessage()) );
	}
	
	public ApiErros(BusinessException ex) {
		this.errors = Arrays.asList(ex.getMessage());
	}

	public ApiErros(ResponseStatusException ex) {
		this.errors = Arrays.asList(ex.getReason());
	}
	
	public List<String> getErrors() {
		return errors;
	}

}
