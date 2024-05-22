package com.shuttleconsulting.fileuploadedapi.repository;

import com.shuttleconsulting.fileuploadedapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findUserEntitiesByUsername(String username);

}
