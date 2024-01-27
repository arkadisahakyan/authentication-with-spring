package arkadisahakyan.authenticationwithspring.repository;

import arkadisahakyan.authenticationwithspring.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
