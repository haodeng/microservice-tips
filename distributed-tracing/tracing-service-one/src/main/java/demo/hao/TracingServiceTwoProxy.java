package demo.hao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "tracing-service-two", path = "/hi")
public interface TracingServiceTwoProxy {

    @GetMapping
    String sayHi();
}
