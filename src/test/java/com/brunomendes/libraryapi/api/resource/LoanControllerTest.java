package com.brunomendes.libraryapi.api.resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.brunomendes.libraryapi.api.dto.LoanDTO;
import com.brunomendes.libraryapi.api.dto.LoanFilterDTO;
import com.brunomendes.libraryapi.api.dto.ReturnedLoanDTO;
import com.brunomendes.libraryapi.exception.BusinessException;
import com.brunomendes.libraryapi.model.entity.Book;
import com.brunomendes.libraryapi.model.entity.Loan;
import com.brunomendes.libraryapi.service.BookService;
import com.brunomendes.libraryapi.service.LoanService;
import com.brunomendes.libraryapi.service.LoanServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = LoanController.class)
public class LoanControllerTest {

	static final String LOAN_API = "/api/loans";

	@Autowired
	MockMvc mvc;

	@MockBean
	private BookService bookService;

	@MockBean
	private LoanService loanService;

	@Test
	@DisplayName("Deve realizar um emprestimo ")
	public void createLoanTest() throws Exception {
		Book book = Book.builder().id(1l).isbn("123").build();
		Loan loan = Loan.builder().id(1l).customer("Fulano").book(book).loanDate(LocalDate.now()).build();
		LoanDTO dto = LoanDTO.builder().isbn("123").email("customer@mail.com").customer("Fulano").build();
		String json = new ObjectMapper().writeValueAsString(dto);

		BDDMockito.given(bookService.getBookByIsbn("123")).willReturn(Optional.of(book));

		BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mvc.perform(request).andExpect(status().isCreated()).andExpect(content().string("1"));

	}

	@Test
	@DisplayName("Deve retornar erro ao tentar fazer emprestimo de um livro inexistente.")
	public void invalidIsbnCreateLoanTest() throws Exception {

		LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
		String json = new ObjectMapper().writeValueAsString(dto);

		BDDMockito.given(bookService.getBookByIsbn("123")).willReturn(Optional.empty());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("errors", Matchers.hasSize(1)))
				.andExpect(jsonPath("errors[0]").value("Book not found for passed isbn"));
	}

	@Test
	@DisplayName("Deve retornar erro ao tentar fazer emprestimo de um livro emprestado.")
	public void loanedBookErrorOnCreateLoanTest() throws Exception {

		LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
		String json = new ObjectMapper().writeValueAsString(dto);

		Book book = Book.builder().id(1l).isbn("123").build();
		BDDMockito.given(bookService.getBookByIsbn("123")).willReturn(Optional.of(book));

		BDDMockito.given(loanService.save(Mockito.any(Loan.class)))
				.willThrow(new BusinessException("Book already loaned"));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("errors", Matchers.hasSize(1)))
				.andExpect(jsonPath("errors[0]").value("Book already loaned"));
	}

	@Test
	@DisplayName("Deve retornar um livro")
	public void returnBookTest() throws Exception {
		ReturnedLoanDTO dto = ReturnedLoanDTO.builder().returned(true).build();
		Loan loan = Loan.builder().id(1l).build();
		BDDMockito.given(loanService.getById(Mockito.anyLong())).willReturn(Optional.of(loan));
		String json = new ObjectMapper().writeValueAsString(dto);

		mvc.perform(patch(LOAN_API.concat("/1")).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());

		Mockito.verify(loanService, Mockito.times(1)).update(loan);
	}

	@Test
	@DisplayName("Deve retornar 404 quando tentar devolver um livro inexistente. ")
	public void returnInexistentBookTest() throws Exception {
		ReturnedLoanDTO dto = ReturnedLoanDTO.builder().returned(true).build();

		BDDMockito.given(loanService.getById(Mockito.anyLong())).willReturn(Optional.empty());

		String json = new ObjectMapper().writeValueAsString(dto);

		mvc.perform(patch(LOAN_API.concat("/1")).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isNotFound());

	}

	@Test
	@DisplayName("Deve filtrar livros")
	public void findLoansTest() throws Exception {
		Long id = 1l;
		Loan loan = LoanServiceTest.createLoan();
		loan.setId(id);
		Book book = Book.builder().id(1l).isbn("321").build();
		loan.setBook(book);
		;

		BDDMockito.given(loanService.find(Mockito.any(LoanFilterDTO.class), Mockito.any(Pageable.class)))
				.willReturn(new PageImpl<Loan>(Arrays.asList(loan), PageRequest.of(0, 10), 1));

		String queryString = String.format("?isbn=%s&customer=%s&page=0&size=10", book.getIsbn(), loan.getCustomer());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(LOAN_API.concat(queryString))
				.accept(MediaType.APPLICATION_JSON);

		mvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("content", Matchers.hasSize(1)))
				.andExpect(jsonPath("totalElements").value(1)).andExpect(jsonPath("pageable.pageSize").value(10))
				.andExpect(jsonPath("pageable.pageNumber").value(0));
	}
}
