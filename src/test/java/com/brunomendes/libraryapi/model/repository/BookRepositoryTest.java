package com.brunomendes.libraryapi.model.repository;

import java.util.Optional;

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

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	BookRepository repository;
	
    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
	public void returnTrueWhenIsbnExists() {
		String isbn = "123";
		Book book = createNewBook(isbn);
		entityManager.persist(book);
		
		boolean exists = repository.existsByIsbn(isbn);
	
		Assertions.assertThat(exists).isTrue();
	}

    
    @Test
    @DisplayName("Deve retornar false quando não existir um livro na base com o isbn informado")
	public void returnFalseWhenIsbnExist() {
        String isbn = "123";
        boolean exists = repository.existsByIsbn(isbn);

        Assertions.assertThat(exists).isFalse();
	}
    
    @Test
    @DisplayName("Deve obter um livro por id")
    public void findByIdTest() {
        Book book = createNewBook("123");
        entityManager.persist(book);

        Optional<Book> foundBook = repository.findById(book.getId());

        Assertions.assertThat(foundBook.isPresent()).isTrue();
    	
    }
    
    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
    	Book book = createNewBook("123");
    	
    	Book savedBook = repository.save(book);
    	
    	Assertions.assertThat(savedBook.getId()).isNotNull();
    }
    
    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest() {
		Book book = createNewBook("123");
		entityManager.persist(book);
		
		Book foundBook = entityManager.find(Book.class, book.getId());
		
		repository.delete(foundBook);
		
		Book deleteBook = entityManager.find(Book.class, book.getId());
		Assertions.assertThat(deleteBook).isNull();
		
    }
    
	public static Book createNewBook(String isbn) {
		return Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
	}
}
