package com.dkhai.fileupload.service;

import com.dkhai.fileupload.controller.response.AuthResponse;
import com.dkhai.fileupload.domain.UserInfo;
import com.dkhai.fileupload.repository.UserRepository;
import com.dkhai.fileupload.security.JwtUtils;
import com.dkhai.fileupload.security.model.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Value("${upload.config.max-size}")
    private Long maxSize;

    @Value("${upload.config.time-per-day}")
    private Long timePerDay;

    @Override
    public AuthResponse authentication(String username, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return AuthResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getUsername())
                .token(jwtUtils.generateJwtToken(authentication))
                .build();
    }

    @Override
    @Transactional
    public UserInfo createUser(UserInfo userInfo) {

        userInfo = userRepository.save(userInfo.toBuilder()
                .password(encoder.encode(userInfo.getPassword()))
                .maxSize(maxSize)
                .used(0l)
                .timeUploadPerDay(timePerDay)
                .build());

        return userInfo;
    }
}
