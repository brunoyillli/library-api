package com.brunomendes.libraryapi.service.impl;

import com.brunomendes.libraryapi.model.entity.Loan;
import com.brunomendes.libraryapi.model.repository.LoanRepository;
import com.brunomendes.libraryapi.service.LoanService;

public class LoanServiceImpl implements LoanService {
	
	private LoanRepository repository;
	
	public LoanServiceImpl(LoanRepository repository) {
		this.repository = repository;
	}

	@Override
	public Loan save(Loan loan) {
		return repository.save(loan);
	}

}
