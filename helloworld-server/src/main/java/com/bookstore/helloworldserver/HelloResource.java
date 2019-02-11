package com.bookstore.helloworldserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello/server")
public class HelloResource {

	@GetMapping
	public String hello() {
		return "Hello World!!";
	}
}
