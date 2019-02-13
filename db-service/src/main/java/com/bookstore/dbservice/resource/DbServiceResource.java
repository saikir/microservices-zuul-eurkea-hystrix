package com.bookstore.dbservice.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.dbservice.model.Books;
import com.bookstore.dbservice.repository.BooksRepository;

@RestController
@RequestMapping("/dbservice")
public class DbServiceResource {

	@Autowired
	BooksRepository bookRepository;

	@GetMapping("/showbooks")
	public List<Books> showBooks() {
		List<Books> myBooks = (List<Books>) bookRepository.findAll();
		System.out.println(myBooks);
		return myBooks;
	}
	
	@GetMapping("/findbystatus/{status}")
	public List<Books> findByStatus(@PathVariable("status") final String status) {
		System.out.println(status);
		return bookRepository.findByStatus(status);
	}
}
