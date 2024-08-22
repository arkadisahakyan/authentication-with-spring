package arkadisahakyan.authenticationwithspring.services;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileUploadServiceIT {

    @Autowired
    private IFileUploadService fileUploadService;

    private String uploadedFileURI;

    @Test
    @Order(1)
    void uploadFileAndReturnLocationWithStatusCode201() {
        MultipartFile file = new MockMultipartFile("file", "hello.txt", "text/plain", "hello world".getBytes());
        ResponseEntity<String> result = fileUploadService.uploadFile(file);
        assertEquals(result.getStatusCode(), HttpStatus.CREATED);
        uploadedFileURI = result.getHeaders().getLocation().getPath();
        assertTrue(uploadedFileURI.matches("/files/.+\\.txt"));
    }

    @Test
    @Order(2)
    void serveFileAndReturnContentTypeWithStatusCode200() throws IOException {
        ResponseEntity<Resource> result = fileUploadService.serveFile(uploadedFileURI.replaceFirst("/files/", ""));
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertTrue(result.getBody().isFile());
        assertEquals(result.getBody().getContentAsString(StandardCharsets.UTF_8), "hello world");
    }

    @AfterAll
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(fileUploadService.getUploadDir().toFile());
    }

    @TestConfiguration
    static class Configuration {
        @Bean
        public IFileUploadService fileUploadService() {
            return new FileUploadService(Paths.get("upload-dir-test"));
        }
    }
}