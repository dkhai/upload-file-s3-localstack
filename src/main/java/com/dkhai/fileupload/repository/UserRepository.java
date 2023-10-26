package com.dkhai.fileupload.repository;

import com.dkhai.fileupload.domain.UserInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserInfo, Long> {

    Optional<UserInfo> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
