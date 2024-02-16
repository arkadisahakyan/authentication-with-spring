package arkadisahakyan.authenticationwithspring.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    //@PreAuthorize("hasAnyAuthority(\"USER\", \"ADMIN\")")
    @GetMapping
    public String user() {
        return "user_page";
    }
}
