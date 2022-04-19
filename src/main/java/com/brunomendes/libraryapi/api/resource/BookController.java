package com.brunomendes.libraryapi.api.resource;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.brunomendes.libraryapi.api.dto.BookDTO;
import com.brunomendes.libraryapi.api.exception.ApiErros;
import com.brunomendes.libraryapi.exception.BusinessException;
import com.brunomendes.libraryapi.model.entity.Book;
import com.brunomendes.libraryapi.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
	
	private BookService service;
	private ModelMapper modelMapper; 
	
	
	public BookController(BookService service, ModelMapper mapper) {
		this.service = service;
		this.modelMapper = mapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO create( @RequestBody @Valid BookDTO dto) {
		Book entity = modelMapper.map(dto, Book.class);
		entity = service.save(entity);
		return modelMapper.map(entity, BookDTO.class);
	}
	
	
	@GetMapping("{id}")
	public BookDTO get(@PathVariable Long id) {
		
		return service.getById(id)
				.map( book -> modelMapper.map(book, BookDTO.class) )
				.orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		Book book = service.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		service.delete(book);
	}
	
	@PutMapping("{id}")
	public BookDTO update( @PathVariable Long id, BookDTO dto) {
        return service.getById(id).map( book -> {

            book.setAuthor(dto.getAuthor());
            book.setTitle(dto.getTitle());
            book = service.update(book);
            return modelMapper.map(book, BookDTO.class);

        }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErros handleValidationExceptions(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
				
		return new ApiErros(bindingResult);
	}
	
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErros handleBusinessException(BusinessException ex) {
		return new ApiErros(ex);
	}
}
