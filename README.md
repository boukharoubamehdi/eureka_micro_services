# Swift Description:

### Disclaimer: 

Originally these project are in separate github project and you can check them and their commits/pr in the section down below:

The goal of this project is to gather in one place all these projects

![eureka-arch](https://user-images.githubusercontent.com/3310864/211613822-7ea88ffa-7935-4e9a-b06c-fe84546617b4.png)


### A brief description of the architecture:

- api gateway:
	- [api-gateway](https://github.com/boukharoubamehdi/api-gateway)  
	
- Eureka server:
	- [naming-server](https://github.com/boukharoubamehdi/naming-server) 

- 2 microservices:
	- [currency-conversion-service](https://github.com/boukharoubamehdi/currency-conversion-service) 
	- [currency-exchange-service](https://github.com/boukharoubamehdi/currency-conversion-service) 

	- FYI: Circuit breaker and FeignClient have been implemented in the microservices.
	
- Configuration:
	- [currency-conversion-service](https://github.com/boukharoubamehdi/currency-conversion-service)
	- [spring-cloud-config-files](https://github.com/boukharoubamehdi/spring-cloud-config-files)
	
- logging and monitoring:
	- Zipkin: is distributed tracing system
	- RabbitMQ: Message Queuing
	- Spring Sleuth: "allows you to aggregate and track log entries as requests move through a distributed software system"
	
- Containerization:
	- Docker
	- Docker compose
  

### Detailed description:


"Eureka Server is an application that holds the information about all client-service applications. 
Every Micro service will register into the Eureka server and Eureka server knows all the client applications running on each port and IP address.
Eureka Server is also known as Discovery Server."

Eureka server:

[naming-server](https://github.com/boukharoubamehdi/naming-server)

The microservices communicates between each other via a FeignClient example:
- [FeignClient](https://github.com/boukharoubamehdi/currency-conversion-service/blob/main/src/main/java/com/mb/rest/webservices/currencyconversionservice/conversion/CurrencyExchangeProxy.java) 


and uses cicruit breaker to handle

"Circuit breakers are a design pattern to create resilient microservices by limiting the impact of service failures and latencies. 
The major aim of the Circuit Breaker pattern is to prevent any cascading failure in the system. In a microservice system, failing fast is critical."

[cicruit breaker](https://github.com/boukharoubamehdi/currency-conversion-service/blob/main/src/main/java/com/mb/rest/webservices/currencyconversionservice/conversion/Controller.java)





Configuration:

Spring cloud servers contains to projects:
- [currency-conversion-service](https://github.com/boukharoubamehdi/currency-conversion-service)
- [spring-cloud-config-files](https://github.com/boukharoubamehdi/spring-cloud-config-files)


limits-service is a client of spring-cloud server and its configuration are stored as an example  in the project spring-cloud-files 


- [limits-service](https://github.com/boukharoubamehdi/limits-service)

microservices:

- [currency-conversion-service](https://github.com/boukharoubamehdi/currency-conversion-service) 
- [currency-exchange-service](https://github.com/boukharoubamehdi/currency-conversion-service) 

