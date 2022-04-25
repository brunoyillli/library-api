package com.brunomendes.libraryapi.api.resource;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.brunomendes.libraryapi.api.dto.LoanDTO;
import com.brunomendes.libraryapi.model.entity.Book;
import com.brunomendes.libraryapi.model.entity.Loan;
import com.brunomendes.libraryapi.service.BookService;
import com.brunomendes.libraryapi.service.LoanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
	
	private final LoanService service;
	private final BookService bookService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Long create( @RequestBody LoanDTO dto ) {
		Book book = bookService.getBookByIsbn(dto.getIsbn()).get();
		Loan entity = Loan.builder()
				.book(book)
				.custumer(dto.getCustomer())
				.loanDate(LocalDate.now())
				.build();
		entity = service.save(entity);
		
		return entity.getId();
	}
}
