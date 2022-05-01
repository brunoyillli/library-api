package com.brunomendes.libraryapi.model.repository;

import static com.brunomendes.libraryapi.model.repository.BookRepositoryTest.createNewBook;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.brunomendes.libraryapi.model.entity.Book;
import com.brunomendes.libraryapi.model.entity.Loan;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {
	
    @Autowired
    private LoanRepository repository;

    @Autowired
    private TestEntityManager entityManager;
	
	
	@Test
	@DisplayName("Deve verificar se existe emprestimo não devolvido para o livro.")
	public void existsByBookAndNotReturnedTest() {
		
		Loan loan = createAndPersistLoan();
		Book book = loan.getBook();
		
		boolean exists = repository.existsByBookAndNotReturned(book);
		
		Assertions.assertThat(exists).isTrue();
		
	}

	
	@Test
	@DisplayName("Deve buscar emprestimos pelo isbn do livro ou customer")
	public void findByBookIsbnOrCustomerTest() {
		Loan loan = createAndPersistLoan();
		
		Page<Loan> result = repository.findByBookIsbnOrCustomer(
				loan.getBook().getIsbn(), loan.getCustomer(), PageRequest.of(0, 10));

		Assertions.assertThat(result.getContent()).hasSize(1);
		Assertions.assertThat(result.getContent()).contains(loan);
		Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);
		Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
		Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
	}
	
	private Loan createAndPersistLoan() {
		Book book = createNewBook("123");
		entityManager.persist(book);
		
		Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(LocalDate.now()).build();
		entityManager.persist(loan);
		return loan;
	}
}
