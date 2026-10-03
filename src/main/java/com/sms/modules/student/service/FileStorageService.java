package com.sms.modules.student.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.core.io.Resource;

public interface FileStorageService {
    String storeFile(MultipartFile file, String filename) throws IOException;

    Resource loadFileAsResource(String storageKey);

    void deleteFile(String storageKey);
}