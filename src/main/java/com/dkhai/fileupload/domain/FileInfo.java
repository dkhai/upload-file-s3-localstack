package com.dkhai.fileupload.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("file_info")
@Builder
public class FileInfo {

    @Id
    private Long id;
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private boolean isUploadSuccessFull;
    private Long userId;

}
