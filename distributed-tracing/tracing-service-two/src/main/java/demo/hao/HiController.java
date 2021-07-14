package demo.hao;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {

    @GetMapping("/hi")
    public String sayHi() {
        return "Hi, from tracing-service-two";
    }

}
