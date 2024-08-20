package arkadisahakyan.authenticationwithspring.services;

import arkadisahakyan.authenticationwithspring.repository.RoleRepository;
import arkadisahakyan.authenticationwithspring.security.AuthorityConstant;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserManagementService implements IUserManagementService {
    protected final static String ACTION_SAVE = "saveButton";
    protected final static String ACTION_DELETE = "deleteButton";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserManagementService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private User retrieveUserFromMultiValueMap(MultiValueMap<String, String> map) {
        Long id = Utilities.parseLong(map.getFirst("id"));
        String username = Utilities.nullableString(map.getFirst("username"));
        String password = Utilities.nullableString(map.getFirst("password"));
        User user = new User(id, username, password);
        List<Role> roles = map.getFirst("roles") == null ? null : Arrays.stream(map.getFirst("roles")
                        .replaceAll("\\s", "")
                        .toLowerCase()
                        .split(","))
                .distinct()
                .map(roleName -> roleRepository.findByRoleNameIgnoreCase(roleName))
                .collect(Collectors.toList());
        // add default role USER
        if (!roles.contains(roleRepository.findByRoleNameIgnoreCase(AuthorityConstant.USER.name())))
            roles.add(roleRepository.findByRoleNameIgnoreCase(AuthorityConstant.USER.name()));
        user.setRoles(roles);
        return user;
    }

    @Override
    public String manageUser(MultiValueMap<String, String> formData, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User changedUserData = retrieveUserFromMultiValueMap(formData);
        String actionName = formData.containsKey(ACTION_SAVE) ? ACTION_SAVE : formData.containsKey(ACTION_DELETE) ? ACTION_DELETE : null;
        if (changedUserData.getId() != null) {
            try {
                if (ACTION_SAVE.equals(actionName)) {
                    userRepository.save(changedUserData);
                }
                else if (ACTION_DELETE.equals(actionName))
                    userRepository.deleteById(changedUserData.getId());
            } catch (RuntimeException exception) {
                redirectAttributes.addFlashAttribute("userSaveFailed", "Failed to save the user");
            }
        }
        return "redirect:" + request.getHeader("referer");
    }
}
