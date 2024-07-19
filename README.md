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

## CHAPTER 3: Adding Spring Cloud Config
### Introduction to Spring Cloud Config

**Spring Cloud Config** provides server-side and client-side support for externalized configuration in a distributed system. It allows microservices to use a centralized configuration server.

<br/>

### Setting Up Spring Cloud Config Server
1- Create a Spring Cloud Config Server Project:
<br/>

- Use [Spring Initializr](https://start.spring.io/) to generate a new project with the **Config Server** dependency. <br/>
- Name the project config-server.
<br/>

2- Setup Config Server: <br/>
open the project and add the @EnableConfigServer annotation to the main application class:

```Java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

<br/>

3- Configure Config Server:
<br/>
Update application.properties:
<br/>

```Java
spring.application.name=config-server
server.port=8888
spring.profiles.active=native
spring.cloud.config.server.native.searchlocations =classpath:config/
```
note: in these configigurations we are using .properties file to store the configurations locally on the server. <br/>
In *resources* folder create config folder : <br/>
../resources/config <br/>
add the following .properties files: <br/>
- microservice1.properties <br/>
in this file add configurations required in microservice 1. For the sake of this boilerplate, i will add api endpoints from microservice 2: <br/>
service2.data.endpoint=/microservice2 <br/>
microservice2.properties<br/>
service1.data.endpoint=/microservice1
<br/>

### Integrate the services with the Spring Cloud Config
<br/>

**Microservice 1** <br/>

```Java
@SpringBootApplication
@EnableDiscoveryClient
public class Microservice1Application {

	public static void main(String[] args) {
		SpringApplication.run(Microservice1Application.class, args);
	}

	@Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

<br/>

```Java
@RestController
public class Microservice_1_Controller {

    // Api endpoint from microservice 2 we want to access from this service.
    @Value("${service2.data.endpoint}")
    private String service2url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/microservice1")
    public String getMessage() {
        return "Message from Microservice 1";
    }
    @GetMapping("/callService2")
    public String callService2() {
        // Discover service instances of microservice2
        List<ServiceInstance> instances = discoveryClient.getInstances("microservice2");

        if (instances != null && !instances.isEmpty()) {
            ServiceInstance serviceInstance = instances.get(0);
            String url = "http://" + serviceInstance.getServiceId() + service2url;
            // ResponseEntity<String> resoinse = restTemplate.getForEntity(url,
            // String.class);
            // return resoinse.toString();
            return restTemplate.getForObject(url, String.class);
        } // String url = "http://localhost:8082"
        return "Service 2 not available";
    }
}
```

<br/>

**Microservice 2**  <br/>

```Java

@SpringBootApplication
@EnableDiscoveryClient
public class Microservice2Application {

	public static void main(String[] args) {
		SpringApplication.run(Microservice2Application.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
```

<br/>

```Java
@RestController
public class Microservice_2_Controller {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service1.data.endpoint}")
    private String service1Url;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/microservice2")
    public String getMessage() {
        return "Message from Microservice 2";
    }

    @GetMapping("/callService1")
    public String callService1() {
        // Discover service instances of microservice1
        List<ServiceInstance> instances = discoveryClient.getInstances("microservice1");
        if (instances != null && !instances.isEmpty()) {
            ServiceInstance serviceInstance = instances.get(0);
            String url = "http://" + serviceInstance.getServiceId() + service1Url;
            return restTemplate.getForObject(url, String.class);
        } else {
            return "No instances available for microservice1";
        }
}
}
```

## CHAPTER 4 : Adding Circuit Breaker with Resilience4j

1- Include the Resilience4j dependecy in pom.xml in each microservice: <br/>

```Xml
<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId	>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
		</dependency>
```
2- In the main class, add this : <br/>

```Java
  @Bean
  public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
    return factory -> factory.configureDefault(id -> 
        new Resilience4JConfigBuilder(id)
        // Set error rate for triggering the circuit breaker to 20% 
        // (default value: 50%)
        .circuitBreakerConfig(CircuitBreakerConfig.custom()
        .failureRateThreshold(20).build())
        .build());
```

3- In the controller Class add the following : <br/>

a. 
```Java
 @Autowired
    private Resilience4JCircuitBreakerFactory circuitBreakerFactory;
```

b. Wrap the endpoint resonse with the circuit breaker: <br/>

```Java
 return circuitBreakerFactory.create("service2").run(() -> 
                restTemplate.getForObject(url, String.class), 
                throwable -> "Service 2 is currently unavailable. Please try again later.");
```

<br/>

### Usage
- Run Eureka Server
- Run Spring Cloud Config Server
- Run Microservice1
- Run Microservice2

  <br/>
  [http://localhost:8081/microservice1](http://localhost:8081/microservice1) should display the message : This message is from microservice 1 <br/>
  However, when we hit [http://localhost:8081/callService2](http://localhost:8081/callService2)  this message will be shown : This message is from microservice 2

 [http://localhost:8082/microservice2](http://localhost:8082/microservice2) should display the message : This message is from microservice 2 <br/>
 However, when we hit  [http://localhost:8082/callService1](http://localhost:8081/callService1) this message will be shown : This message is from microservice 1 <br/>

with this setup so far we made sure everything works fine !


