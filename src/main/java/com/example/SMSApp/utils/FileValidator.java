package com.example.SMSApp.utils;

import com.example.SMSApp.exception.custom.FileValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileValidator {

    public static void validateFile(MultipartFile file, List<String> allowedMimeTypes, long maxSizeInBytes, String fieldName) {
        if (file.isEmpty()) {
            throw new FileValidationException(fieldName + " must not be empty.");
        }

        if (file.getSize() > maxSizeInBytes) {
            throw new FileValidationException(fieldName + " must not exceed " + (maxSizeInBytes / (1024 * 1024)) + "MB.");
        }

        String mimeType = file.getContentType();
        if (mimeType == null || !allowedMimeTypes.contains(mimeType.toLowerCase())) {
            throw new FileValidationException(fieldName + " must be of types: " + allowedMimeTypes);
        }
    }
}
