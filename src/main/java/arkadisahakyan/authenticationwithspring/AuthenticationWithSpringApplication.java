package arkadisahakyan.authenticationwithspring;

import arkadisahakyan.authenticationwithspring.model.Role;
import arkadisahakyan.authenticationwithspring.model.User;
import arkadisahakyan.authenticationwithspring.repository.RoleRepository;
import arkadisahakyan.authenticationwithspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
			User user = new User(Long.valueOf(1), "user", "user");
			userRepository.save(user);
			roleRepository.save(new Role(Long.valueOf(1), user, "USER"));
		}
		if (userRepository.findByUsername("admin") == null) {
			User admin = new User(Long.valueOf(2), "admin", "admin");
			userRepository.save(admin);
			roleRepository.save(new Role(Long.valueOf(2), admin, "ADMIN"));
		}
	}
}
