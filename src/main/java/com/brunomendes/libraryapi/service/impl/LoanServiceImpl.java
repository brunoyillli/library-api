package com.brunomendes.libraryapi.service.impl;

import com.brunomendes.libraryapi.exception.BusinessException;
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
		if( repository.existsByBookAndNotReturned(loan.getBook()) ) {
			throw new BusinessException("Book already loaned");
		}
		return repository.save(loan);
	}

}