package com.brunomendes.libraryapi.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.brunomendes.libraryapi.api.dto.BookDTO;
import com.brunomendes.libraryapi.model.entity.Book;
import com.brunomendes.libraryapi.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
	
	private BookService service;
	
	
	
	public BookController(BookService service) {
		this.service = service;
	}



	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO create( @RequestBody BookDTO dto) {
		Book entity = Book.builder()
				.author(dto.getAuthor())
				.title(dto.getTitle())
				.isbn(dto.getIsbn())
				.build();
		
		entity = service.save(entity);
		
		return BookDTO.builder()
				.id(entity.getId())
				.author(entity.getAuthor())
				.title(entity.getTitle())
				.isbn(entity.getIsbn())
				.build();
	}

}
