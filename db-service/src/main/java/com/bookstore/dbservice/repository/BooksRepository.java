package com.bookstore.dbservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bookstore.dbservice.model.Books;

public interface BooksRepository extends CrudRepository<Books, Integer>{

	/*@Query("SELECT * FROM Books ORDER BY id")
	List<Book> showAll();*/
	
	@Query("SELECT b FROM Books b WHERE b.status LIKE :status")
	List<Books> findByStatus(@Param("status") String status);

}
