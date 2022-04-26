package com.brunomendes.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.brunomendes.libraryapi.exception.BusinessException;
import com.brunomendes.libraryapi.model.entity.Book;
import com.brunomendes.libraryapi.model.repository.BookRepository;
import com.brunomendes.libraryapi.service.impl.BookServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
	
	BookService service;
	
	@MockBean
	BookRepository repository;
	
	@BeforeEach
	public void setUp() {
		this.service = new BookServiceImpl( repository );
	}
	
	@Test
	@DisplayName("Deve salvar um livro")
	public void saveBookTest() {
		Book book = createValidBook();
		Mockito.when( repository.save(book) ).thenReturn(
				Book.builder().id((long) 11)
				.isbn("123")
				.title("As aventuras")
				.author("Fulano").build()
		);
		
		
		Book savedBook = service.save(book);
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getIsbn()).isEqualTo("123");
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
		
	}
	
    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN(){

    	Book book = createValidBook();
        when( repository.existsByIsbn(Mockito.anyString()) ).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(book);

    }
    
    @Test
    @DisplayName("Deve obter um livro por id")
    public void getByIdTest() {
    	Long id = 1l;
    	Book book = createValidBook();
    	book.setId(id);
    	Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));
    	
    	Optional<Book> foundBook = service.getById(id);
    	
    	assertThat( foundBook.isPresent() ).isTrue();
    	assertThat( foundBook.get().getId()).isEqualTo(id);
    	assertThat( foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
    	assertThat( foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    	assertThat( foundBook.get().getTitle()).isEqualTo(book.getTitle());
    	
    }
    
    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por id quando ele não existe na base.")
    public void bookNotFoundByIdTest() {
    	Long id = 1l;

    	Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
    	
    	Optional<Book> book = service.getById(id);
    	
    	assertThat( book.isPresent() ).isFalse();

    }
    
    @Test
    @DisplayName("Deve deletar um livro.")
    public void deleteBookTest(){
        Book book = Book.builder().id(1l).build();


        org.junit.jupiter.api.Assertions.assertDoesNotThrow( () -> service.delete(book) );

        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer erro a	o tentar deletar um livro inexistente.")
    public void deleteInvalidBookTest(){
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        Mockito.verify( repository, Mockito.never() ).delete(book);
    }
    
    @Test
    @DisplayName("Deve ocorrer erro a	o tentar atualizar um livro inexistente.")
    public void updateInvalidBookTest(){
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));

        Mockito.verify( repository, Mockito.never() ).save(book);
    }
    
    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest() {
    	long id = 1l;
		Book updatingBook = Book.builder().id(id).build();
    	
    	Book updateBook = createValidBook();
    	updateBook.setId(id);
    	
    	Mockito.when(repository.save(updatingBook)).thenReturn(updateBook);
    	
    	Book book = service.update(updatingBook);
    	
    	assertThat(book.getId()).isEqualTo(updateBook.getId());
    	assertThat(book.getTitle()).isEqualTo(updateBook.getTitle());
    	assertThat(book.getIsbn()).isEqualTo(updateBook.getIsbn());
    	assertThat(book.getAuthor()).isEqualTo(updateBook.getAuthor());    		
    }
    @SuppressWarnings("unchecked")
	@Test
    @DisplayName("Deve filtrar livros pelas propriedades")
    public void findBookTest() {
    	
    	Book book = createValidBook();
    	book.setId(1l);
        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Book> lista = Arrays.asList(book);
        Page<Book> page = new PageImpl<Book>(lista, pageRequest, 1);
    	
    	Mockito.when( repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
        .thenReturn(page);
    	Page<Book> result = service.find( book, pageRequest );
    	
    	assertThat(result.getTotalElements()).isEqualTo(1);
    	assertThat(result.getContent()).isEqualTo(lista);
    	assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
    	assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }
    
    @Test
    @DisplayName("Deve obter um livro pelo isbn")
    public void getBookByIsbnTest() {
    	String isbn = "1230";
    	Mockito.when(repository.findByIsbn(isbn)).thenReturn( Optional.of(Book.builder().id(1l).isbn(isbn).build()) );
    	
    	Optional<Book> book = service.getBookByIsbn(isbn);
    	
    	assertThat(book.isPresent()).isTrue();
    	assertThat(book.get().getId()).isEqualTo(1l);
    	assertThat(book.get().getIsbn()).isEqualTo(isbn);
    	
    	verify(repository, Mockito.times(1)).findByIsbn(isbn);	
    }
    
    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }
}
