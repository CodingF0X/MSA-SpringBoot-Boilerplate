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
