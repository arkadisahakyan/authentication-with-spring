package arkadisahakyan.authenticationwithspring.repository;

import arkadisahakyan.authenticationwithspring.model.Role;
import arkadisahakyan.authenticationwithspring.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Collection<Role> findByUserId(User userId);
}
