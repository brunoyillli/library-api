package com.brunomendes.libraryapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brunomendes.libraryapi.model.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>{

}
