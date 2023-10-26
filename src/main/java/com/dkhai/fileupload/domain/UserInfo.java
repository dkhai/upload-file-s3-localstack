package com.dkhai.fileupload.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_info")
@Getter
@Builder(toBuilder = true)
public class UserInfo {

    @Id
    private Long id;
    private String username;
    private String email;
    private String password;
    private Long maxSize;
    private Long used;
    private Long timeUploadPerDay;
}
