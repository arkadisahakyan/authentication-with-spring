package arkadisahakyan.authenticationwithspring.controllers;

import arkadisahakyan.authenticationwithspring.model.Role;
import arkadisahakyan.authenticationwithspring.model.User;
import arkadisahakyan.authenticationwithspring.repository.RoleRepository;
import arkadisahakyan.authenticationwithspring.repository.UserRepository;
import arkadisahakyan.authenticationwithspring.security.AuthorityConstant;
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
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private RememberMeServices rememberMeServices;

    @GetMapping
    public String register() {
        return "register";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String registerUser(@RequestBody MultiValueMap<String, String> formData, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirect) {
        String username = formData.getFirst("username");
        String password = formData.getFirst("password");
        String passwordConfirm = formData.getFirst("password-confirm");
        if (isRegistrationFailed(username, password, passwordConfirm, redirect))
            return "redirect:" + request.getHeader("referer");
        registerAndRememberUser(username, password, request, response);
        logger.info("New user registered");
        return "redirect:" + request.getHeader("referer");
    }

    public boolean isRegistrationFailed(String username, String password, String passwordConfirm, RedirectAttributes redirect) {
        boolean failed = false;
        if (userRepository.findByUsername(username) != null) {
            redirect.addFlashAttribute("usernameAlreadyExists", true);
            failed = true;
        }
        if (!password.matches("^.{8,}$")) {
            redirect.addFlashAttribute("invalidPasswordFormat", true);
            failed = true;
        }
        if (!password.equals(passwordConfirm)) {
            redirect.addFlashAttribute("passwordMismatch", true);
            failed = true;
        }
        return failed;
    }

    private void registerAndRememberUser(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        // invalidate session - prevent session fixation attacks
        request.getSession().invalidate();
        // save user to database
        User newUser = new User(0L, username, password);
        newUser.setRoles(List.of(roleRepository.findByRoleNameIgnoreCase(AuthorityConstant.USER.name())));
        userRepository.save(newUser);
        // remember user - create remember-me token
        Authentication formAuth = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(formAuth);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, request, response);
        rememberMeServices.loginSuccess(request, response, formAuth);
    }
}