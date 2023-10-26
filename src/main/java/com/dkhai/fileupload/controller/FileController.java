package com.dkhai.fileupload.controller;

import com.dkhai.fileupload.domain.FileInfo;
import com.dkhai.fileupload.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<?> getAllFiles() {

        //todo: return response instead of entity
        return ResponseEntity.ok().body(fileStorageService.findAllFilesByUser());
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("files") MultipartFile[] files) throws IOException {

        // todo: checking avail upload

        long totalSize = 0L;
        for (MultipartFile file : files) {
            totalSize += file.getSize();
        }
        if (fileStorageService.isUploadQuotaAvailable(totalSize)) {
            // todo: handle exception
            throw new RuntimeException();
        }

        List<FileInfo> infoList = fileStorageService.upload(files);
        return ResponseEntity.ok().body(infoList);
    }
}
