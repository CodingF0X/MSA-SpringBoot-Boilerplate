package demo.microservice2.ms2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
