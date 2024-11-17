package arkadisahakyan.blog.controllers;

import arkadisahakyan.blog.model.User;
import arkadisahakyan.blog.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = {"/", "home"})
    public String home(HttpServletRequest request, Authentication authentication, Model model) {
        Iterable<User> allUsers = userRepository.findAll();
        model.addAttribute("usersList", allUsers);
        return "home";
    }
}
