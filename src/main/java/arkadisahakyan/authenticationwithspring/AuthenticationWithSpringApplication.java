package arkadisahakyan.authenticationwithspring;

import arkadisahakyan.authenticationwithspring.controllers.FileUploadController;
import arkadisahakyan.authenticationwithspring.model.Role;
import arkadisahakyan.authenticationwithspring.model.User;
import arkadisahakyan.authenticationwithspring.repository.RoleRepository;
import arkadisahakyan.authenticationwithspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class AuthenticationWithSpringApplication implements ApplicationRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationWithSpringApplication.class, args);
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
		Path uploadDir = FileUploadController.uploadDir;
		if (!Files.exists(uploadDir))
			Files.createDirectories(uploadDir);
	}
}
