package arkadisahakyan.blog.dto;

import arkadisahakyan.blog.model.Role;
import arkadisahakyan.blog.model.User;

import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private List<Role> roles;

    public UserDTO() {
    }

    public UserDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserDTO(Long id, String username, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public UserDTO(User author) {
        this.id = author.getId();
        this.username = author.getUsername();
        this.roles = author.getRoles();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
