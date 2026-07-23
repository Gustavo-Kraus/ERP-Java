package dev.kraus.ERP.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "redirect:/main";
    }

    @GetMapping("/login")
    public String login() {
        return "custom-login";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

}
