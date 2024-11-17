package arkadisahakyan.blog.controllers;

import arkadisahakyan.blog.model.User;
import arkadisahakyan.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAnyAuthority(\"ADMIN\")")
    @GetMapping
    public String admin(Model model) {
        Iterable<User> allUsers = userRepository.findAll();
        model.addAttribute("usersList", allUsers);
        return "admin_page";
    }
}
