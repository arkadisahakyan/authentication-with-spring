package arkadisahakyan.authenticationwithspring.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class HomeController {

    @GetMapping(value = {"/", "home"})
    public String home(HttpServletRequest request, Authentication authentication) {
        return "home";
    }
}
