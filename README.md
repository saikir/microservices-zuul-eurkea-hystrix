# microservices-zuul-eurkea-hystrix


Microservices is a service oriented design pattern.
which improves the flexibility in deploying the application. 


Reduces the configurations makes the application production ready.
Makes the application highly available at any cost.



Eureka registry server: Netflix open source application. which used for service discovery (without having the server knowledge.)
Dependencies:
	
	1. To generate this one, we need to create a spring boot application with "eureka server" as a dependency.
	2. Add @EnableEurekaServer annotation to the application.java class.
	3. Add application.yml file to not register this eureka server as one of the service and add below lines.
		eureka:
		  client:
		    register-with-eureka: false
		    fetch-registry: false
		  server:
		    wait-time-in-ms-when-sync-empty: 0
		
	4. Add spring.application.name and server.port numbers in application.properties file.


Helloworld-server:
	
	1. Add eureka-discovery and mvc as dependency and add @EnableDiscoveryClient annotation to application class.
	2. Add application.yml file and config:
		spring:
		  application:
		    name: hello-server
		server:
		  port: 8901
		  
		eureka:
		  client:
		    register-with-eureka: true
		    fetch-registry: true
		    service-url:
		      defaultZone: http://localhost:8761/eureka/
		  

Helloworld-client:
	
	1. Add eureka-discovery and mvc as dependency and add @EnableDiscoveryClient annotation to application class.
	2. Add application.yml file and config:
		spring:
		  application:
		    name: hello-client
		server:
		  port: 8902
		
		eureka:
		  client:
		    register-with-eureka: true
		    fetch-registry: true
		    service-url:
		      defaultZone: http://localhost:8761/eureka/
		
	3. Add Configuration class into SpringApplication class.
		@org.springframework.context.annotation.Configuration
		class Config {
			@LoadBalanced
			@Bean
			RestTemplate restTemplate(){
				return new RestTemplate();
			}
		}
		
		
		
		The below code in HelloResource() class in client.
		@GetMapping
		public String hello() {
		String url = "http://hello-server/hello/server";
		return restTemplate.getForObject(url, String.class);
		}
		
		
		The hello-server is registered in eureka discovery, so there is no need to mention the ports and host names.
		
		To call the server we need a RestTemplate in the client, which can be autowired because before eureka/spring cloud loads we need to load resttemplate for that we need to use configuration.
		
		The above lines should be added in rest client which helps the eureka server to know that this is the rest template I need to control to.
		
		
Hystrix:
Production ready implementation of the circuit-breaker pattern for reducing the impact of the failure and latency in distributed systems.
*Handles the external service calls*.

Implementation of Hystrix:
	
	1. Add Hystrix Dependency and add @EnableCircuitBreaker to application class.
	2. Add @HystrixCommand(fallbackMethod = "fallback") annotation to the method which calls to external services. and we 		need to create a method name "fallback".
	Note: The fallback method should return the same object which the annotated method calls. 
	3. Throwable argument helps to find the exception for calling the fallback.

	@HystrixCommand(fallbackMethod="fallback")
	@GetMapping
	public String hello() {
	String url = "http://hello-server/hello/server";
	return restTemplate.getForObject(url, String.class);
	}
	public String fallback(Throwable hystrixCommand) {
	return "This is Hystrix command fallback method. Please check the health of hello server";
	}
	

Zuul:
Is an API-gateway for the application.





Catalog Service
hostname:8081/catalog

hostname:8080/api
API-gateway
Zuul(in 8080)

Service Registry
Eureka

Request

Image Service
hostname:8082/image

Payment Service
hostname:8083/payment

Using Zuul we can call the /catalog service without knowing its hostname and port number by.

hostname:8080/api/catalog 

Implementation of Zuul:
	
	1. Add Zuul as dependency and add @EnableZuulProxy annotation to the application.
	2. Add application.yml file as below.
		spring:
		  application:
		    name: zuul-service
		server:
		  port: 8903
		  
		eureka:
		  client:
		    register-with-eureka: true
		    fetch-registry: true
		    service-url:
		      defaultZone: http://localhost:8761/eureka/
		      
		zuul:
		  prefix: /api
		  routes:
		    server:
		      path: /server/**
		      service-id: hello-server
		    client:
		      path: /client/**
		      service-id: hello-client
		  
		
DB-Service:
		  
Implementing dbservice:
	
	1. Add mysql, jpa and mvc as dependency.
	2. Add below in application.properties.
		spring.datasource.url=jdbc:mysql://localhost:3306/stockprices
		spring.datasource.username=root
		spring.datasource.password =zxcvbnmm
		spring.datasource.testWhileIdle=true
		spring.datasource.validationQuery=SELECT 1
		spring.jpa.show-sql=true
		#spring.jpa.hibernate.ddl-auto=update
		spring.jpa.hibernate.naming-strategy=org.hibernate.cfg.ImprovedNamingStrategy
		spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
		
	3. Add @EnableJpaRepositories(basePackages = "com.bookstore.dbservice.repository")
	4. DbServiceRepository.java
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
		@RequestMapping("/dbservice/")
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
		a. 
	5. BookRepository.java
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
		
	6. Books.java (entity class)
		package com.bookstore.dbservice.model;
		
		import javax.persistence.*;
		
		@Entity
		@Table(name="books", catalog="mymc")
		public class Books {
		
		@Id
		@Column(name="book_id", unique=true)
		@GeneratedValue(strategy=GenerationType.AUTO)
		private int Id;
		@Column(name="book_name")
		private String bookName;
		@Column(name="author_name")
		private String author;
		@Column(name="genre")
		private String genre;
		@Column(name="status")
		private String status;
		
		public int getId() {
		return Id;
		}
		
		public void setId(int id) {
		Id = id;
		}
		
		public String getBookName() {
		return bookName;
		}
		
		public void setBookName(String bookName) {
		this.bookName = bookName;
		}
		
		public String getAuthor() {
		return author;
		}
		
		public void setAuthor(String author) {
		this.author = author;
		}
		
		public String getGenre() {
		return genre;
		}
		
		public void setGenre(String genre) {
		this.genre = genre;
		}
		
		public String getStatus() {
		return status;
		}
		
		public void setStatus(String status) {
		this.status = status;
		}
		
		public Books(int id, String bookName, String author, String genre, String status) {
		super();
		Id = id;
		this.bookName = bookName;
		this.author = author;
		this.genre = genre;
		this.status = status;
		}
		
		protected Books() {
		}
		
		@Override
		public String toString() {
		return "Books [Id=" + Id + ", bookName=" + bookName + ", author=" + author + ", genre=" + genre + ", status="
		+ status + "]";
		}
		}
		
		
Testing:
http://localhost:8903/api/client/hello/client/findbooksbystatus/Available

the above url call using zuul host and port http://localhost:8903, prefix /api, client service /client, request mapping path in client /hello/client, getmapping method /findbookbystatus/{status} which calls the db-service by rest template in hello client.
