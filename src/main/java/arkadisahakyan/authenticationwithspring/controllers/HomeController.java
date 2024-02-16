package arkadisahakyan.authenticationwithspring.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class HomeController {

    @GetMapping(value = {"/", "home"})
    public String home(Authentication authentication) {
        System.out.println("Authentication: " + authentication);
        // Output: Authentication: null
        System.out.println("Authentication: " + SecurityContextHolder.getContext().getAuthentication());
        // Output: AnonymousAuthenticationToken [Principal=anonymousUser, Credentials=[PROTECTED], Authenticated=true...
        return "home";
    }
}
