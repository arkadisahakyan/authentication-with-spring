package arkadisahakyan.blog.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void admin_Should_Redirect_If_Not_Authenticated() throws Exception {
        mockMvc.perform(get("/admin")).andDo(print()).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    void admin_Access_Denied_If_Not_Authorized() throws Exception {
        mockMvc.perform(get("/admin")).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void admin_Access_Allowed_If_Authorized() throws Exception {
        mockMvc.perform(get("/admin")).andDo(print()).andExpect(status().isOk());
    }
}