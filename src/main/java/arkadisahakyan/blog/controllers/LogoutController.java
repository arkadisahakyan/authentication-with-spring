package arkadisahakyan.blog.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/logout")
public class LogoutController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @GetMapping
    public String logout() {
        if (SecurityContextHolder.getContext().getAuthentication() == null || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
            || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)
            return "redirect:/";
        return "logout";
    }


}
