package arkadisahakyan.authenticationwithspring.configuration;

import arkadisahakyan.authenticationwithspring.model.Article;
import arkadisahakyan.authenticationwithspring.model.Role;
import arkadisahakyan.authenticationwithspring.model.User;
import arkadisahakyan.authenticationwithspring.repository.ArticleRepository;
import arkadisahakyan.authenticationwithspring.repository.RoleRepository;
import arkadisahakyan.authenticationwithspring.repository.UserRepository;
import arkadisahakyan.authenticationwithspring.services.FileUploadService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;

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
        if (userRepository.findByUsername("user") == null) {
            User user = new User(1L, "user", "user");
            userRepository.save(user);
            roleRepository.save(new Role(1L, user, "USER"));
        }
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User(2L, "admin", "admin");
            userRepository.save(admin);
            roleRepository.save(new Role(2L, admin, "ADMIN"));
        }

        // create upload directory if not exists
        Path uploadDir = FileUploadService.uploadDir;
        if (!Files.exists(uploadDir))
            Files.createDirectories(uploadDir);

        // create sample articles
        for (int i = 1; i <= 101; i++) {
            articleRepository.save(new Article(0L, "Sample #" + i, "Some text...", null, null, new User(2L)));
        }
    }
}
