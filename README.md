# MSA-SpringBoot-Demo
<br/>

**This project is part of of my lab assignments for Software Intensive Solution lab @ Dortmund University of Applied Sciences and Arts. In that lab, I developed a full-fledged distributed system using **Microservice Architecture and N-Tier Architecture**. Also utilized Domain Driven Design for the system design and modeling.**

<br/>

This project is a boilerplate and will demonstrates the integration of the two services using Eureka and Spring Cloud Config. It will also demonstrate how Circuit breaker pattern work which is used in microservices architecture to prevent cascading failures.

<br/>
<br/>

**In this guide, i will devide the development into 5 chapters. This to make it easy follow up and make the work more organized.**

## CHAPTER 1 - Implementing Microservices using Spring Boot
### Introduction to Spring Boot
Spring Boot is a framework for building Java applications. It simplifies the setup and development of new Spring applications by providing a range of default configurations. <br/>

### Creating Microservice 1
1- Create a Spring Boot Project: <br/>
Use [Spring Initializr](https://start.spring.io/) to generate a new project with the following dependencies: Spring Web, Spring Boot DevTools .  ( Later we will add more dependecies as we progress ).
<br/>

2- Setup Project Structure: <br/>
Unzip the generated project and open it in your favorite IDE (I'm usiing VS Code).
<br/>

3- [Microservice 1 Configuration](https://github.com/CodingF0X/MSA-SpringBoot-demo/blob/feature-microservice1/microservice1/Microservice1.md).


### Creating Microservice 2
Follow the same steps for creating Microservice 1

[Microservice 2 Configuration](https://github.com/CodingF0X/MSA-SpringBoot-demo/blob/feature-microservice2/microservice2/Microservice2.md).

## CHAPTER 2 - Adding Eureka to the Solution
### Introduction to Eureka
Eureka is a service registry from Netflix. It helps in locating services by their name for the purpose of load balancing and failover of middle-tier servers.

<br/>

### Setting Up Eureka Server
1- Create a Eureka Server Project:

<br/>

2- Use [Spring Initializr](https://start.spring.io/) to generate a new project with the Eureka Server dependency.<br/>
Name the project eureka-server.

<br/>

3- Configure Eureka server : [Eureka Configuration](https://github.com/CodingF0X/MSA-SpringBoot-demo/blob/main/Eureka-Server/HELP.md).

<br/>

4- Registering Microservices with Eureka:

<br/>

in pom.xml file of each service, add the following: <br/>

**a.** 

```xml
 <properties>
		<java.version>17</java.version>
		<spring-cloud.version>2023.0.2</spring-cloud.version>
	</properties>
```

<br/>

**b. Add spring cloud management dependencies :**

<br/>

```xml
<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>${spring-cloud.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
```

<br/>

**c. Add Spring and netflix dependencies :** 

<br/>

```xml
	   <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-config</artifactId>
     </dependency>

    <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
   </dependency>
```

<br/> 

**d. In main class of each service, add @EnableDiscoveryClient** 

<br/> 

```Java
@SpringBootApplication
@EnableDiscoveryClient
public class Microservice1Application {

	public static void main(String[] args) {
		SpringApplication.run(Microservice1Application.class, args);
	}
}
```

<br/>

**e. in application.properties file add the following:** 

<br/>

```java
spring.application.name=microservice1
server.port=8081
spring.config.import=optional:configserver:http://localhost:8888
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka
eureka.instance.hostname=localhost
eureka.instance.preferIpAddress=true
eureka.client.fetch-registry=true
```
<br/> 
Do the same for microservice2 !

