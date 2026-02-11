package com.velox.onlinefilemanager.controller;

import com.velox.onlinefilemanager.entity.FileInfo;
import com.velox.onlinefilemanager.service.FileStorageService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class FileManagerController {
    @Autowired
    private FileStorageService fileStorageService;
    private static final Logger log = Logger.getLogger(FileManagerController.class.getName());


    @PostMapping("/upload-file")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file){

        try {

            fileStorageService.saveFile(file);

            return ResponseEntity.ok("Upload successful");

        } catch (Exception e){

            return ResponseEntity
                    .badRequest()
                    .body("Upload failed");
        }
    }




    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String filename) {
        log.log(Level.INFO, "[Normal] Downloading file with /download: " + filename);
        try {
            var fileToDownload = fileStorageService.getDownloadFile(filename);

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentLength(fileToDownload.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(Files.newInputStream(fileToDownload.toPath())));

        }catch (IOException e){
            log.log(Level.SEVERE,"Exception during download file", e.getMessage());
            return ResponseEntity.notFound().build();
        }

    }



    @GetMapping("/download-faster")
    public ResponseEntity<Resource> downloadFileFaster(@RequestParam("fileName") String filename) {

        log.log(Level.INFO, "[FASTER] Downloading file with /download-faster: " + filename);
        try {
            var fileToDownload = fileStorageService.getDownloadFile(filename);

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentLength(fileToDownload.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new FileSystemResource(fileToDownload));

        }catch (IOException e){
            log.log(Level.SEVERE,"Exception during download file", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/files")
    public List<FileInfo> files() {
        return fileStorageService.listFiles();
    }

    @DeleteMapping("/delete-file")
    public ResponseEntity<?> deleteFile(
            @RequestParam String fileName){

        fileStorageService.delete(fileName);

        return ResponseEntity.ok().build();
    }
}

