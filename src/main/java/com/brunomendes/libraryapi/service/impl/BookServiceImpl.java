package com.brunomendes.libraryapi.service.impl;

import org.springframework.stereotype.Service;

import com.brunomendes.libraryapi.model.entity.Book;
import com.brunomendes.libraryapi.model.repository.BookRepository;
import com.brunomendes.libraryapi.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	private BookRepository repository;

	public BookServiceImpl(BookRepository repository) {
		this.repository = repository;
	}

	@Override
	public Book save(Book book) {

		return repository.save(book);
	}

}
