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
		  
		
