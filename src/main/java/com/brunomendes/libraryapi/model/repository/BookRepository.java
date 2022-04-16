package com.brunomendes.libraryapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brunomendes.libraryapi.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}
