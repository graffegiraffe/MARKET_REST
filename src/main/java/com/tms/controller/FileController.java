package com.tms.controller;

import com.tms.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/file")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> uploadFile(@RequestParam("file") MultipartFile file) {
        logger.info("Received request to upload a file: {}", file.getOriginalFilename());
        Boolean result = fileService.uploadFile(file);
        if (result) {
            logger.info("File {} uploaded successfully", file.getOriginalFilename());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            logger.error("Failed to upload file: {}", file.getOriginalFilename());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        logger.info("Received request to download file: {}", filename);
        Optional<Resource> resource = fileService.getFile(filename);
        if (resource.isPresent()) {
            logger.info("File {} found and ready for download", filename);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.get().getFilename());
            return new ResponseEntity<>(resource.get(), headers, HttpStatus.OK);
        } else {
            logger.warn("File {} not found", filename);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<ArrayList<String>> getListOfFiles() {
        logger.info("Received request to list all files");
        ArrayList<String> files;
        try {
            files = fileService.getListOfFiles();
            if (files.isEmpty()) {
                logger.warn("No files found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                logger.info("Files retrieved: {}", files);
                return new ResponseEntity<>(files, HttpStatus.OK);
            }
        } catch (IOException e) {
            logger.error("Error occurred while retrieving files: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<HttpStatus> deleteFile(@PathVariable("filename") String filename) {
        logger.info("Received request to delete file: {}", filename);
        Boolean result = fileService.deleteFile(filename);
        if (result) {
            logger.info("File {} deleted successfully", filename);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.error("Failed to delete file: {}", filename);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}