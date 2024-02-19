package arkadisahakyan.authenticationwithspring.controllers;

import arkadisahakyan.authenticationwithspring.model.Role;
import arkadisahakyan.authenticationwithspring.model.User;
import arkadisahakyan.authenticationwithspring.repository.RoleRepository;
import arkadisahakyan.authenticationwithspring.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/register")
public class RegisterController {

    private static final Logger logger = LogManager.getLogger(RegisterController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @GetMapping
    public String register() {
        return "register";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String registerUser(@RequestBody MultiValueMap<String, String> formData, HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("REGISTER USER");
        String username = formData.getFirst("username");
        String password = formData.getFirst("password");
        String passwordConfirm = formData.getFirst("password-confirm");
        if (userRepository.findByUsername(username) != null) {
            logger.info("UNSUCCESSFUL: username already taken");
            model.addAttribute("usernameAlreadyTaken", true);
            return "register";
        }
        if (!password.equals(passwordConfirm)) {
            logger.info("UNSUCCESSFUL: passwords don't match");
            model.addAttribute("passwordMismatch", true);
            return "register";
        }
        // save user to database
        User newUser = new User(0L, username, password);
        userRepository.save(newUser);
        roleRepository.save(new Role(0L, newUser, "USER"));
        // remember user
        Authentication formAuth = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(formAuth);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        // invalidate session - prevent session fixation attacks
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        request.getSession().invalidate();
        securityContextRepository.saveContext(securityContext, request, response);
        logger.info("SUCCESSFUL: new user created");
        return "register";
    }
}