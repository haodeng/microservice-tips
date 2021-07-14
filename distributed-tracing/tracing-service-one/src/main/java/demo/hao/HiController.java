package demo.hao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {
    @Autowired
    private TracingServiceTwoProxy tracingServiceTwoProxy;

    @GetMapping("/hi")
    public String sayHi() {
        return tracingServiceTwoProxy.sayHi();
    }

}
