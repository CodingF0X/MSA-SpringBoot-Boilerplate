# MSA-SpringBoot-Demo
## Microservice 1
### Configurations 
1- Create The Rest Controller Class and define the api endpoint.
```Java
package demo.microservice1.ms1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Microservice1Controller {

    @GetMapping("/microservice1")
    public String getMessage(){
        return "This message is from microservice 1";
    }
}
```
<br/>

2- in application.properties file set the port to 8081: <br/>
spring.application.name=microservice1 <br/>
server.port=8081
<br/>
