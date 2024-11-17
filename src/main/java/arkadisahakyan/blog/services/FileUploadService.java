package arkadisahakyan.blog.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadService implements IFileUploadService {
    private final Path uploadDir;

    public FileUploadService() {
        this.uploadDir = Paths.get("upload-dir");
    }

    public FileUploadService(Path uploadDir) {
        this.uploadDir = uploadDir;
    }

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file) {
        createUploadDirectoryIfNotExists();
        try {
            String filename = file.getOriginalFilename();
            String fileSuffix = filename.substring(filename.lastIndexOf('.'));
            Path fileDest = Files.createTempFile(uploadDir, null, fileSuffix);
            Files.copy(file.getInputStream(), fileDest, StandardCopyOption.REPLACE_EXISTING);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/files/" + fileDest.subpath(1, fileDest.getNameCount()).toString().replace('\\', '/')));
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void createUploadDirectoryIfNotExists() {
        if (!Files.exists(uploadDir)) {
            try {
                System.out.println("CREATED");
                Files.createDirectories(uploadDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ResponseEntity<Resource> serveFile(String filename) {
        Path fileDest = uploadDir.resolve(filename);
        Resource fileAsResource;
        try {
            fileAsResource = new UrlResource(fileDest.toUri());
            if (!fileAsResource.exists())
                throw new Exception("File doesn't exist.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
        MediaType contentType;
        try {
            contentType = MediaType.valueOf(Files.probeContentType(fileDest));
        } catch (Exception e) {
            contentType = MediaType.TEXT_HTML;
        }
        return ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileDest.getFileName() + "\"")
                .body(fileAsResource);
    }

    public Path getUploadDir() {
        return uploadDir;
    }
}
