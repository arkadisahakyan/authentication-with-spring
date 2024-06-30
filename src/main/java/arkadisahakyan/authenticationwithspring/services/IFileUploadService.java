package arkadisahakyan.authenticationwithspring.services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadService {
    ResponseEntity<String> uploadFile(MultipartFile file);
    ResponseEntity<Resource> serveFile(String filename);
}
