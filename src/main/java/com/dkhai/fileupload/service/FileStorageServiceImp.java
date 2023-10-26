package com.dkhai.fileupload.service;

import com.dkhai.fileupload.domain.FileInfo;
import com.dkhai.fileupload.domain.UserInfo;
import com.dkhai.fileupload.repository.FileInfoRepository;
import com.dkhai.fileupload.repository.UserRepository;
import com.dkhai.fileupload.security.model.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImp implements FileStorageService {

    private final AwsS3Service awsS3Service;
    private final FileInfoRepository repository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public List<FileInfo> upload(MultipartFile[] files) throws IOException {

        List<FileInfo> fileInfos = new ArrayList<>();
        for (MultipartFile file : files) {

            fileInfos.add(this.upload(file));
        }
        return fileInfos;
    }

    @Override
    public FileInfo upload(MultipartFile file) throws IOException {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        String fileName = userDetails.getId() + "/" + Objects.requireNonNull(file.getOriginalFilename())
                .replaceAll("[\\W_]+", "");
        if (!awsS3Service.uploadObjectToS3(fileName, file.getBytes())) {

            // todo: throw exception
            throw new RuntimeException("Cannot upload!");
        }

        FileInfo fileInfo = repository.save(FileInfo.builder()
                .fileName(file.getOriginalFilename())
                .fileUrl(fileName)
                .fileSize(file.getSize())
                .userId(userDetails.getId())
                .isUploadSuccessFull(true) // default as true, will use later in event-driven
                .build());

        Optional<UserInfo> userOpt = userRepository.findById(userDetails.getId());
        if (userOpt.isPresent()) {

            UserInfo userInfo = userOpt.get();
            userRepository.save(userInfo.toBuilder().used(userInfo.getUsed() + file.getSize()).build());

        }

        return fileInfo;
    }

    @Override
    public List<FileInfo> findAllFilesByUser() {

        // todo: get current userID, return only file from that user
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return repository.findByUserId(userDetails.getId());
    }

    @Override
    public boolean isUploadQuotaAvailable(Long size) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Optional<UserInfo> userOpt = userRepository.findById(userDetails.getId());

        if (userOpt.isPresent()) {

            UserInfo userInfo = userOpt.get();
            return userInfo.getUsed() + size > userInfo.getMaxSize();

        }

        return false;
    }


}
