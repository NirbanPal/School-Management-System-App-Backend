package com.example.SMSApp.support.storage;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


@Component
@Slf4j
public class FileStorageService {


    @Value("${file.upload-dir}")
    private String baseUploadDir;

    private final List<String> subFolders = List.of("cv", "idproof", "profile");

    @PostConstruct
    public void initDirectories() {
        try {
            Path basePath = Paths.get(baseUploadDir).toAbsolutePath().normalize();
            Files.createDirectories(basePath);
            for (String folder : subFolders) {
                Path subPath = basePath.resolve(folder);
                if (!Files.exists(subPath)) {
                    Files.createDirectories(subPath);
                    log.info("Created upload subdirectory: {}", subPath);
                }
            }
        } catch (IOException e) {
            log.error("Failed to create upload directories", e);
            throw new RuntimeException("Could not initialize upload directories.", e);
        }
    }

    public String storeFile(MultipartFile file, String subFolder, UUID fileId) {

        String newFileName = fileId+getCleanFileName(file);

        try {
            Path targetLocation = Paths.get(baseUploadDir, subFolder).toAbsolutePath().normalize();
            Files.createDirectories(targetLocation);
            Path targetFilePath = targetLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);

            return targetFilePath.toString();
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + getCleanFileName(file), ex);
        }
    }

    public String getFileExtension(MultipartFile file) {
        String name = file.getOriginalFilename();
        return name != null && name.contains(".")
                ? name.substring(name.lastIndexOf('.') + 1)
                : "unknown";
    }

    public String getCleanFileName(MultipartFile file) {
        return StringUtils.cleanPath(file.getOriginalFilename());
    }

    // If you want to delete all the file related to that UUID
    public void deleteFilesByUuid(UUID pid) {
        if (pid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        for (String subFolder : subFolders) {
            Path directory = Paths.get(baseUploadDir, subFolder).toAbsolutePath().normalize();
            try (Stream<Path> files = Files.list(directory)) {
                files.filter(path -> {
                    String filename = path.getFileName().toString();
                    return filename.startsWith(pid.toString());
                }).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                        log.info("Deleted file: {}", path);
                    } catch (IOException e) {
                        log.error("Failed to delete file: {}", path, e);
                    }
                });
            } catch (IOException e) {
                log.error("Error accessing directory: {}", directory, e);
            }
        }
    }

    //If you want to delete a specific file.
    public void deleteFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return;
        }
        try {
            Path path = Paths.get(filePath).toAbsolutePath().normalize();
            Files.deleteIfExists(path);
            log.info("Deleted file: {}", path);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filePath, e);
        }
    }

}

