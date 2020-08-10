package io.reflectoring.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
class HelloController {

    @GetMapping("/")
    ModelAndView hello(){
        Map<String, Object> model = new HashMap<>();
        model.put("name", "Bob");
        return new ModelAndView("hello.html", model);
    }

}
