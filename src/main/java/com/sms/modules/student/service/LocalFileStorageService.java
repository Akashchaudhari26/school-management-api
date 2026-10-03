package com.sms.modules.student.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path storageDirectory;

    public LocalFileStorageService(@Value("${app.file-storage.path:./data/student-documents}") String storagePath) {
        this.storageDirectory = Path.of(storagePath).toAbsolutePath().normalize();
    }

    @Override
    public String storeFile(MultipartFile file, String filename) throws IOException {
        Files.createDirectories(storageDirectory);
        String originalName = filename == null ? "" : StringUtils.cleanPath(filename);
        String extension = StringUtils.getFilenameExtension(originalName);
        String storageKey = UUID.randomUUID() + (extension == null ? "" : "." + extension);
        Path destination = resolve(storageKey);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return storageKey;
    }

    @Override
    public Resource loadFileAsResource(String storageKey) {
        Path path = resolve(storageKey);
        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("File not found");
        }
        return new PathResource(path);
    }

    @Override
    public void deleteFile(String storageKey) {
        try {
            Files.deleteIfExists(resolve(storageKey));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to delete stored file", exception);
        }
    }

    private Path resolve(String storageKey) {
        String safeKey = StringUtils.cleanPath(storageKey == null ? "" : storageKey);
        Path path = storageDirectory.resolve(safeKey).normalize();
        if (safeKey.isBlank() || !path.startsWith(storageDirectory) || path.equals(storageDirectory)) {
            throw new IllegalArgumentException("Invalid file key");
        }
        return path;
    }
}
