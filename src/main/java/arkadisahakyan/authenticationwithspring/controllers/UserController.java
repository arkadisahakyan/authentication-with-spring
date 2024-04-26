package arkadisahakyan.authenticationwithspring.controllers;

import arkadisahakyan.authenticationwithspring.Utilities;
import arkadisahakyan.authenticationwithspring.model.Role;
import arkadisahakyan.authenticationwithspring.model.User;
import arkadisahakyan.authenticationwithspring.repository.RoleRepository;
import arkadisahakyan.authenticationwithspring.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private final static String ACTION_SAVE = "saveButton";
    private final static String ACTION_DELETE = "deleteButton";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public String user() {
        return "user_page";
    }

    public User retrieveUserFromMultiValueMap(MultiValueMap<String, String> formData) {
        Long id = Long.valueOf(formData.getFirst("id"));
        String username = Utilities.nullableString(formData.getFirst("username"));
        String password = Utilities.nullableString(formData.getFirst("password"));
        User changedUserData = new User(id, username, password);
        List<Role> roles = Arrays.stream(formData.getFirst("roles").split(","))
                .map(roleName -> new Role(0L, changedUserData, roleName))
                .collect(Collectors.toList());
        changedUserData.setRoles(roles);
        return changedUserData;
    }

    @PreAuthorize("hasAnyAuthority(\"ADMIN\")")
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String manageUser(@RequestBody MultiValueMap<String, String> formData, HttpServletRequest request, RedirectAttributes redirect) {
        Long userId = Long.valueOf(formData.getFirst("userId"));
        User changedUserData = retrieveUserFromMultiValueMap(formData);
        String actionName = formData.getFirst(ACTION_SAVE) != null ? ACTION_SAVE : ACTION_DELETE;
        if (ACTION_SAVE.equals(actionName)) {
            try {
                userRepository.save(changedUserData);
            } catch (DataAccessException exception) {
                redirect.addFlashAttribute("userSaveFailed", true);
            }
        } else if (ACTION_DELETE.equals(actionName)) {
            userRepository.deleteById(userId);
        }
        return "redirect:" + request.getHeader("referer");
    }
}
