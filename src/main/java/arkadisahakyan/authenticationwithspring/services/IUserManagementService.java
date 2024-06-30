package arkadisahakyan.authenticationwithspring.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface IUserManagementService {
    String manageUser(MultiValueMap<String, String> formData, HttpServletRequest request, RedirectAttributes redirectAttributes);
}
