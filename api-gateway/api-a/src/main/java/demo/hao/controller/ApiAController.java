package demo.hao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-a")
public class ApiAController {
    private Logger logger = LoggerFactory.getLogger(ApiAController.class);

    @GetMapping("/ping")
    public String ping() {
        logger.info("api a");
        return "api a";
    }

}
