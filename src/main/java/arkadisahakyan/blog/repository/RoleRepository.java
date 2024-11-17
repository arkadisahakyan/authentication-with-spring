package arkadisahakyan.blog.repository;

import arkadisahakyan.blog.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRoleNameIgnoreCase(String roleName);
}
