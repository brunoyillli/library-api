package com.brunomendes.libraryapi.service;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.brunomendes.libraryapi.model.entity.Book;
import com.brunomendes.libraryapi.model.entity.Loan;
import com.brunomendes.libraryapi.model.repository.LoanRepository;
import com.brunomendes.libraryapi.service.impl.LoanServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

	
	LoanService service;
	
	@MockBean
	LoanRepository repository;
	
	@BeforeEach
	public void setUp() {
		this.service = new LoanServiceImpl(repository);
	}

	@Test
	@DisplayName("Deve salvar um emprestimo")
	public void saveLoanTest() {
		Book book = Book.builder().id(1l).build();
		Loan savingLoan = Loan.builder()
				.book(book)
				.customer("Fulano")
				.loanDate(LocalDate.now())
				.build();
	
	Loan savedLoan = Loan.builder()
			.id(1l)
			.loanDate(LocalDate.now())
			.customer("Fulano")
			.book(book).build();
			
	
	Mockito.when( repository.save(savingLoan) ).thenReturn(savedLoan);
	
	Loan loan = service.save(savingLoan);
	
	assertThat(loan.getId()).isEqualTo(savedLoan.getId());
	assertThat(loan.getBook()).isEqualTo(savedLoan.getBook());
	assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
	assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
	}
}
