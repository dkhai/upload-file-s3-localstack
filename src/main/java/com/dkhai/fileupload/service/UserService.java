package com.dkhai.fileupload.service;

import com.dkhai.fileupload.controller.response.AuthResponse;
import com.dkhai.fileupload.domain.UserInfo;

public interface UserService {

    AuthResponse authentication(String username, String password);

    UserInfo createUser(UserInfo userInfo);
}
