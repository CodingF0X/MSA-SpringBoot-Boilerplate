# Eureka Server
## Setting Up Eureka Server

1- Add the @EnableEurekaServer annotation to your main application class:
```Java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}
}
```
<br/>

2- Configure Eureka Server:
<br/>
Update application.properties:
<br/>

```Java
spring.application.name=Eureka-Server
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```
<br/>
3- Run Eureka Server ! 
<br/>
4- Access Eureka Server dashboard by hitting http://localhost:8761/
