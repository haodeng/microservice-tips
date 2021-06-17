package demo.hao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-b")
public class ApiBController {
    private Logger logger = LoggerFactory.getLogger(ApiBController.class);

    @GetMapping("/ping")
    public String ping() {
        logger.info("api b");
        return "api b";
    }

}
