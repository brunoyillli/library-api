package com.brunomendes.libraryapi.api.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;

public class ApiErros {
	private List<String> errors;
	public ApiErros(BindingResult bindingResult) {
		this.errors = new ArrayList<>();
		bindingResult.getAllErrors().forEach( error -> this.errors.add(error.getDefaultMessage()) );
	}
	
	public List<String> getErrors() {
		return errors;
	}

}
