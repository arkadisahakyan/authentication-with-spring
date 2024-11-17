package arkadisahakyan.blog.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void uploadFile_Should_Redirect_If_Not_Authenticated() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.abc", "text/plain", "hello world".getBytes());
        mockMvc.perform(multipart("/upload")
                        .file(file).with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void uploadFile_Should_Upload_File_And_Return_Path() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.abc", "text/plain", "hello world".getBytes());
        mockMvc.perform(multipart("/upload")
                .file(file).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string(matchesRegex("/files/.+")));
    }

    @Test
    void serveFile() throws Exception {
        mockMvc.perform(get("/files/not_existing_file.abc")).andDo(print()).andExpect(status().is5xxServerError());
    }
}