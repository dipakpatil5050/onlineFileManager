package com.velox.onlinefilemanager.service;

import com.velox.onlinefilemanager.entity.FileInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileStorageService {

    private static final String STORAGE_DIRECTORY =  "E:\\Dipak Data\\Learning Data\\Java and Spring Boot\\File Manager Upload and Download files\\Storage";

    public void saveFile(MultipartFile fileToSave) throws IOException {
        if (fileToSave == null){
            throw new NullPointerException("fileToSave is null");
        }
        var targetFile = new File(STORAGE_DIRECTORY + File.separator + fileToSave.getOriginalFilename());

        if (!Objects.equals(targetFile.getParent(), STORAGE_DIRECTORY)){
                throw new SecurityException("Unsupported filename!");
        }

        Files.copy(fileToSave.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }


    public File getDownloadFile(String fileName) throws IOException {
        if (fileName == null){
            throw new NullPointerException("fileName is null");
        }
        var fileToDownload = new File(STORAGE_DIRECTORY + File.separator + fileName);

        if (!Objects.equals(fileToDownload.getParent(), STORAGE_DIRECTORY)){
            throw new SecurityException("Unsupported filename!");
        }

        if (!fileToDownload.exists()){
            throw new FileNotFoundException("No file named :" + fileName);
        }

        return fileToDownload;
    }


    public List<FileInfo> listFiles() {

        File directory = new File(STORAGE_DIRECTORY);

        if (!directory.exists() || !directory.isDirectory()) {
            return Collections.emptyList();
        }

        return Arrays.stream(directory.listFiles())
                .filter(File::isFile)
                .map(file -> {

                    long sizeInKB = file.length() / 1024;

                    if (sizeInKB == 0) {
                        sizeInKB = 1; // minimum 1 KB
                    }

                    return new FileInfo(
                            file.getName(),
                            sizeInKB
                    );
                })
                .sorted(Comparator.comparing(FileInfo::getName))
                .collect(Collectors.toList());
    }


    public void delete(String fileName) {

        try {

            if (fileName.contains("..")) {
                throw new SecurityException("Invalid file name");
            }

            File file = new File(STORAGE_DIRECTORY, fileName);

            if (!file.exists()) {
                throw new FileNotFoundException("File not found");
            }

            boolean deleted = file.delete();

            if (!deleted) {
                throw new IOException("Unable to delete file");
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to delete file: " + fileName,
                    e
            );
        }
    }

}
