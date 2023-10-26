package com.dkhai.fileupload.repository;

import com.dkhai.fileupload.domain.FileInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {

    List<FileInfo> findByFileName(String name);

    List<FileInfo> findByUserId(Long UserId);
}
