package com.brunomendes.libraryapi.service;

import java.util.Optional;

import com.brunomendes.libraryapi.model.entity.Book;

public interface BookService {

	Book save(Book any);

	Optional<Book> getById(Long id);

	void delete(Book book);

	Book update(Book book);

}
