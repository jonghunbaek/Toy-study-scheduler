package toyproject.studyscheduler.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @RequestMapping("/main")
    public String test () {
        return "main";
    }
}
