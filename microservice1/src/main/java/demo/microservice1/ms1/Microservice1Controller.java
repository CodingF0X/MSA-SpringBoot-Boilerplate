package demo.microservice1.ms1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Microservice1Controller {
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
