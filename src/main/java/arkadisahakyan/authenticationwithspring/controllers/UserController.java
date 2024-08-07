package arkadisahakyan.authenticationwithspring.controllers;

import arkadisahakyan.authenticationwithspring.services.IArticleManagementService;
import arkadisahakyan.authenticationwithspring.services.IUserManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private final static String ACTION_SAVE = "saveButton";
    private final static String ACTION_DELETE = "deleteButton";

    private final IUserManagementService userManagementService;
    private final IArticleManagementService articleManagementService;

    @Autowired
    public UserController(IUserManagementService userManagementService, IArticleManagementService articleManagementService) {
        this.userManagementService = userManagementService;
        this.articleManagementService = articleManagementService;
    }

    @GetMapping
    public String user(Model model) {
        model.addAttribute("articlesList", articleManagementService.getAllArticleSummariesOfCurrentUser(PageRequest.of(0, Integer.MAX_VALUE)));
        return "user_page";
    }

    @PreAuthorize("hasAnyAuthority(\"ADMIN\")")
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String manageUser(@RequestBody MultiValueMap<String, String> formData, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        return userManagementService.manageUser(formData, request, redirectAttributes);
    }
}
