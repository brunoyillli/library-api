package com.brunomendes.libraryapi.api.resource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.brunomendes.libraryapi.api.dto.BookDTO;
import com.brunomendes.libraryapi.api.dto.LoanDTO;
import com.brunomendes.libraryapi.api.dto.LoanFilterDTO;
import com.brunomendes.libraryapi.api.dto.ReturnedLoanDTO;
import com.brunomendes.libraryapi.model.entity.Book;
import com.brunomendes.libraryapi.model.entity.Loan;
import com.brunomendes.libraryapi.service.BookService;
import com.brunomendes.libraryapi.service.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Tag(name = "Loan", description = "the Loan API")
public class LoanController {
	
	private final LoanService service;
	private final BookService bookService;
	private final ModelMapper modelMapper;
	
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar um emprestimo")
    public Long create(@RequestBody LoanDTO dto) {
        Book book = bookService
                .getBookByIsbn(dto.getIsbn())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));
        Loan entity = Loan.builder()
                .book(book)
                .customer(dto.getCustomer())
                .loanDate(LocalDate.now())
                .build();

        entity = service.save(entity);
        return entity.getId();
    }
    
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Busca um emprestimo pelo ID")
    public void returnBook( @PathVariable Long id, @RequestBody ReturnedLoanDTO dto) {
    	Loan loan = service.getById(id).orElseThrow( () -> 
    			new ResponseStatusException(HttpStatus.NOT_FOUND));
    	loan.setReturned(dto.getReturned());
    	
    	service.update(loan);
    }
    
    @GetMapping
    @Operation(summary = "Filtrar emprestimos")
    public Page<LoanDTO> find(LoanFilterDTO dto, Pageable pageRequest){
    	Page<Loan> result = service.find(dto, pageRequest);
    	List<LoanDTO> loans = result
    		.getContent()
    		.stream()
    		.map( entity -> {
    			
    			Book book = entity.getBook();
    			BookDTO bookDTO = modelMapper.map(book,BookDTO.class);
    			LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
    			loanDTO.setBook(bookDTO);
				return loanDTO;
				
    		}).collect(Collectors.toList());
    	return new PageImpl<LoanDTO>(loans, pageRequest, result.getTotalElements());
    }
}
