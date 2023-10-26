package com.dkhai.fileupload.service;

import com.dkhai.fileupload.domain.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileStorageService {

    List<FileInfo> upload(MultipartFile[] files) throws IOException;

    FileInfo upload(MultipartFile file) throws IOException;

    List<FileInfo> findAllFilesByUser();

    boolean isUploadQuotaAvailable(Long size);

}
