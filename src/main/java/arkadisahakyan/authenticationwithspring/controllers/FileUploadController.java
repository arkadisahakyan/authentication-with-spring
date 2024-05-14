package arkadisahakyan.authenticationwithspring.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
public class FileUploadController {
    public static final Path uploadDir = Paths.get("upload-dir");

    @PostMapping(value = "/upload")
    @ResponseBody
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String fileSuffix = filename.substring(filename.lastIndexOf('.'));
            Path fileDest = Files.createTempFile(uploadDir, null, fileSuffix);
            System.out.println(fileDest.toString());
            Files.copy(file.getInputStream(), fileDest, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok()
                    .body("/files/" + fileDest.subpath(1, fileDest.getNameCount()).toString().replace('\\', '/'));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping(value = "/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path fileDest = uploadDir.resolve(filename);
            Resource fileAsResource = new UrlResource(fileDest.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileDest.getFileName() + "\"")
                    .body(fileAsResource);
        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
