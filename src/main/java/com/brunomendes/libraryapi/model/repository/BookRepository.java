package com.brunomendes.libraryapi.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brunomendes.libraryapi.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

    boolean existsByIsbn( String isbn );

	Optional<Book> findByIsbn(String isbn);

}
