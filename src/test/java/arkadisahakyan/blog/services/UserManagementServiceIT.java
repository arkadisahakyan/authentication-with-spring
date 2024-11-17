package arkadisahakyan.blog.services;

import arkadisahakyan.blog.model.Role;
import arkadisahakyan.blog.model.User;
import arkadisahakyan.blog.repository.RoleRepository;
import arkadisahakyan.blog.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DataJpaTest
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
@Import(UserManagementService.class)
class UserManagementServiceIT {

    @SpyBean
    private UserRepository userRepository;

    @SpyBean
    private RoleRepository roleRepository;

    @Autowired
    private IUserManagementService userManagementService;

    private User user;

    @BeforeAll
    void setUp() {
        roleRepository.save(new Role(0L, "USER"));
        roleRepository.save(new Role(0L, "ADMIN"));
        user = userRepository.save(new User(0L, "Arthur", "password", List.of(roleRepository.findByRoleNameIgnoreCase("USER"))));
    }

    @Test
    void changeUserDataInDatabase() {
        // prepare function arguments
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("id", String.valueOf(user.getId()));
        formData.add("username", "George");
        formData.add("password", "qwerty");
        formData.add("roles", "user,admin");
        formData.add(UserManagementService.ACTION_SAVE, "Change the user data");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("referer")).thenReturn("/admin");
        RedirectAttributes redirect = mock(RedirectAttributes.class);
        // test
        assertThat(userManagementService.manageUser(formData, request, redirect)).isEqualTo("redirect:/admin");
        verify(userRepository).save(any(User.class));
        User userWithChangedData = userRepository.findByUsername("George");
        assertThat(userWithChangedData).isNotNull();
        assertThat(userWithChangedData.getId()).isEqualTo(user.getId());
        assertThat(userWithChangedData.getUsername()).isEqualTo("George");
        assertThat(userWithChangedData.getPassword()).isEqualTo("qwerty");
        List<Role> expectedRoles = List.of(roleRepository.findByRoleNameIgnoreCase("USER"), roleRepository.findByRoleNameIgnoreCase("ADMIN"));
        assertTrue(userWithChangedData.getRoles().containsAll(expectedRoles));
    }

    @Test
    void deleteUserFromDatabase() {
        // prepare function arguments
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("id", String.valueOf(user.getId()));
        formData.add("username", user.getUsername());
        formData.add("password", user.getPassword());
        formData.add("roles", user.getPassword());
        formData.add(UserManagementService.ACTION_DELETE, "Delete the user");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("referer")).thenReturn("/admin");
        RedirectAttributes redirect = mock(RedirectAttributes.class);
        // test
        assertThat(userManagementService.manageUser(formData, request, redirect)).isEqualTo("redirect:/admin");
        verify(userRepository).deleteById(user.getId());
        User deletedUser = userRepository.findByUsername("Arthur");
        assertThat(deletedUser).isNull();
    }

    @AfterAll
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}