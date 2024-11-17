package arkadisahakyan.blog.controllers;

import arkadisahakyan.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void user_Should_Redirect_If_Not_Authenticated() throws Exception {
        mockMvc.perform(get("/user")).andDo(print()).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void user_Access_Allowed_If_Authenticated() throws Exception {
        mockMvc.perform(get("/user")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void manageUser_Should_Redirect_If_Not_Authenticated() throws Exception {
        mockMvc.perform(post("/user").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    void manageUser_Access_Denied_If_Not_Authorized() throws Exception {
        mockMvc.perform(post("/user").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void manageUser_Should_Redirect_If_Authorized() throws Exception {
        mockMvc.perform(post("/user").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }
}