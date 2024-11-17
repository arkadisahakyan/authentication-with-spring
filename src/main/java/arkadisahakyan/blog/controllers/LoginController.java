package arkadisahakyan.blog.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @GetMapping
    public String login() {
        return "login";
    }
}
