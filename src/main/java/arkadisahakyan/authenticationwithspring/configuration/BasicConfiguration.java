package arkadisahakyan.authenticationwithspring.configuration;

import arkadisahakyan.authenticationwithspring.model.Article;
import arkadisahakyan.authenticationwithspring.model.Role;
import arkadisahakyan.authenticationwithspring.model.User;
import arkadisahakyan.authenticationwithspring.repository.ArticleRepository;
import arkadisahakyan.authenticationwithspring.repository.RoleRepository;
import arkadisahakyan.authenticationwithspring.repository.UserRepository;
import arkadisahakyan.authenticationwithspring.security.AuthorityConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BasicConfiguration implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // create default roles
        for (AuthorityConstant c : AuthorityConstant.values())
            if (roleRepository.findByRoleNameIgnoreCase(c.name()) == null)
                roleRepository.save(new Role(0L, c.name()));

        // create default users
        if (userRepository.findByUsername("user") == null) {
            User user = new User(1L, "user", "user");
            user.setRoles(List.of(roleRepository.findByRoleNameIgnoreCase(AuthorityConstant.USER.name())));
            userRepository.save(user);
        }
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User(2L, "admin", "admin");
            admin.setRoles(List.of(roleRepository.findByRoleNameIgnoreCase(AuthorityConstant.USER.name()),
                    roleRepository.findByRoleNameIgnoreCase(AuthorityConstant.ADMIN.name())));
            userRepository.save(admin);
        }

        // create sample articles
        for (int i = 1; i <= 101; i++) {
            articleRepository.save(new Article(0L, "Sample #" + i, "Some text...", null, null, new User(2L)));
        }
    }
}
