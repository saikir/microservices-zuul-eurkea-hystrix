package com.bookstore.helloworldclient;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/hello/client")
public class HelloResource {

	@Autowired
	RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod="fallback")
	@GetMapping
	public String hello() {
		String url = "http://hello-server/hello/server";
		return restTemplate.getForObject(url, String.class);
	}
	
	@HystrixCommand(fallbackMethod="dbServiceDown")
	@GetMapping("/findbooksbystatus/{status}")
	public List<Object> findBooksByStatus(@PathVariable("status") final String status) {
		//System.out.println(restTemplate.getForObject("http://db-service/db-service/findbystatus/"+status, List.class));
		System.out.println(restTemplate.getForObject("http://db-service/dbservice/showbooks", List.class));
		return restTemplate.getForObject("http://db-service/dbservice/findbystatus/"+status, List.class);
	}
	
	public String fallback(Throwable hystrixCommand) {
		return "This is Hystrix command fallback method. Please check the health of hello server";
	}
	
	public List<Object> dbServiceDown(String status) {
		List l = new ArrayList();
		l.add("Please check the db service");
		return l;
	}
}
