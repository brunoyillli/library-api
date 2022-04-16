package com.brunomendes.libraryapi.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.brunomendes.libraryapi.model.entity.Book;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
	BookService service;
	Assertions assertions;
	
	@Test
	@DisplayName("Deve salvar um livro")
	public void saveBookTest() {
		Book book = Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
		
		Book savedBook = service.save(book);
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getIsbn()).isEqualTo("123");
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
		
	}
}
