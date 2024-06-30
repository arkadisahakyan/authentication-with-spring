package arkadisahakyan.authenticationwithspring;

import arkadisahakyan.authenticationwithspring.repository.UserRepository;
import arkadisahakyan.authenticationwithspring.services.FileUploadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class AuthenticationWithSpringApplicationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void run_Should_Create_Default_Users() {
        assertNotNull(userRepository.findByUsername("user"));
        assertNotNull(userRepository.findByUsername("admin"));
    }

    @Test
    void run_Should_Create_Upload_Directory() {
        assertTrue(Files.exists(FileUploadService.uploadDir));
    }
}