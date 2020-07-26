package io.reflectoring.thymeleafvue;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
class HelloVueController {

    @GetMapping("/")
    ModelAndView showHelloPage() {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Hello Vue!");
        model.put("chartData", Arrays.asList(7,6,5,4,3,2,1));
        return new ModelAndView("hello-vue.html", model);
    }

}
