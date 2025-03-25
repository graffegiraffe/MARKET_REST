package com.tms.service;

import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final Path ROOT_FILE_PATH = Paths.get("data");

    public Boolean uploadFile(MultipartFile file) {
        try {
            if (file.getOriginalFilename() == null) {
                return false;
            }
            Files.copy(file.getInputStream(), ROOT_FILE_PATH.resolve(file.getOriginalFilename()));
        } catch (IOException exception) {
            return false;
        }
        return true;
    }

    public Optional<Resource> getFile(String fileName) {
        Resource resource = new PathResource(ROOT_FILE_PATH.resolve(fileName));
        if (resource.exists()) {
            return Optional.of(resource);
        }
        return Optional.empty();
    }

    public ArrayList<String> getListOfFiles() throws IOException {
        return (ArrayList<String>) Files
                .walk(ROOT_FILE_PATH, 1)
                .filter(path -> !path.equals(ROOT_FILE_PATH))
                .map(Path::toString).collect(Collectors.toList());
    }

    public Boolean deleteFile(String fileName) {
        Path path = ROOT_FILE_PATH.resolve(fileName);
        File file = new File(path.toString());
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}