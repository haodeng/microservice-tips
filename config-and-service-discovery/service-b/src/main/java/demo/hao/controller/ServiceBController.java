package demo.hao.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service-b")
public class ServiceBController {

    @Value("${property1}")
    String property1;

    @Value("${property2}")
    String property2;

    @GetMapping("/property1")
    public String getProperty1() {
        return property1;
    }

    @GetMapping("/property2")
    public String getProperty2() {
        return property2;
    }
}
