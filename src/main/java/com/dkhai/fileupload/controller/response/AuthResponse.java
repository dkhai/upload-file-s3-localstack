package com.dkhai.fileupload.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private Long id;
    private String username;
    private String email;
    private String token;
}
