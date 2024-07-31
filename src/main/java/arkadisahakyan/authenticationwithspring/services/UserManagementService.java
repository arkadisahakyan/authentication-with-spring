package arkadisahakyan.authenticationwithspring.services;

import arkadisahakyan.authenticationwithspring.util.Utilities;
import arkadisahakyan.authenticationwithspring.model.Role;
import arkadisahakyan.authenticationwithspring.model.User;
import arkadisahakyan.authenticationwithspring.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagementService implements IUserManagementService {
    private final static String ACTION_SAVE = "saveButton";
    private final static String ACTION_DELETE = "deleteButton";

    private final UserRepository userRepository;

    @Autowired
    public UserManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected static User retrieveUserFromMultiValueMap(MultiValueMap<String, String> map) {
        Long id = Utilities.parseLong(map.getFirst("id"));
        String username = Utilities.nullableString(map.getFirst("username"));
        String password = Utilities.nullableString(map.getFirst("password"));
        User user = new User(id, username, password);
        List<Role> roles = map.getFirst("roles") == null ? null : Arrays.stream(map.getFirst("roles")
                        .split(","))
                .map(roleName -> new Role(0L, user, roleName))
                .collect(Collectors.toList());
        user.setRoles(roles);
        return user;
    }

    @Override
    public String manageUser(MultiValueMap<String, String> formData, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User changedUserData = retrieveUserFromMultiValueMap(formData);
        String actionName = formData.getFirst(ACTION_SAVE) != null ? ACTION_SAVE : ACTION_DELETE;
        if (changedUserData.getId() != null) {
            try {
                if (ACTION_SAVE.equals(actionName))
                    userRepository.save(changedUserData);
                else if (ACTION_DELETE.equals(actionName))
                    userRepository.deleteById(changedUserData.getId());
            } catch (RuntimeException exception) {
                redirectAttributes.addFlashAttribute("userSaveFailed", true);
            }
        }
        return "redirect:" + request.getHeader("referer");
    }
}
