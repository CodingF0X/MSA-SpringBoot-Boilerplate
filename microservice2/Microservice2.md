# MSA-SpringBoot-Demo
## Microservice 2
### Configurations 
1- Create The Rest Controller Class and define the api endpoint.
```Java
package demo.microservice2.ms2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Microservice2Controller {

    @GetMapping("/microservice2")
    public String getMessage(){
        return "This message is from microservice 2";
    }
}
```
<br/>

2- in application.properties file set the port to 8082: <br/>
spring.application.name=microservice2 <br/>
server.port=8082
<br/>
