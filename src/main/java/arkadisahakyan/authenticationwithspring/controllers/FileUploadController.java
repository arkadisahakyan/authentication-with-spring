package arkadisahakyan.authenticationwithspring.controllers;

import arkadisahakyan.authenticationwithspring.services.IFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {
    private final IFileUploadService fileUploadService;

    @Autowired
    public FileUploadController(IFileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "/upload")
    @ResponseBody
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) {
        return fileUploadService.uploadFile(file);
    }

    @GetMapping(value = "/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        return fileUploadService.serveFile(filename);
    }
}
