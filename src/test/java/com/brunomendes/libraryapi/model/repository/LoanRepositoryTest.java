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
	@DisplayName("deve verificar se existe emprestimo não devolvido para o livro.")
	public void existsByBookAndNotReturnedTest() {
		
		Book book = createNewBook("123");
		entityManager.persist(book);
		
		Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(LocalDate.now()).build();
		entityManager.persist(loan);
	
		boolean exists = repository.existsByBookAndNotReturned(book);
		
		Assertions.assertThat(exists).isTrue();
		
	}
}
