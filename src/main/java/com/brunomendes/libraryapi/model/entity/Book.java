package com.brunomendes.libraryapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Book {
	private Long id; 
	private String title;
	private String author;
	private String isbn;

}
