package com.example.SMSApp.support.storage;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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


@Service
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

    public String storeFile(MultipartFile file, String subFolder) {

//        String fileExtension = getFileExtension(file);
        String newFileName = UUID.randomUUID()+getCleanFileName(file);

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
        return UUID.randomUUID()+StringUtils.cleanPath(file.getOriginalFilename());
    }

}

