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
