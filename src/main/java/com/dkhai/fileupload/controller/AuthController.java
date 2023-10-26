package com.dkhai.fileupload.controller;

import com.dkhai.fileupload.controller.request.SignInRequest;
import com.dkhai.fileupload.controller.request.SignUpRequest;
import com.dkhai.fileupload.controller.response.MessageResponse;
import com.dkhai.fileupload.domain.UserInfo;
import com.dkhai.fileupload.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody SignInRequest request) {

        return ResponseEntity.ok(userService.authentication(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest request) {

        // todo: do validate user/email if they are in use

        userService.createUser(UserInfo.builder()
                .username(request.getUsername())
                .email(request.getPassword())
                .password(request.getPassword())
                .build());

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
