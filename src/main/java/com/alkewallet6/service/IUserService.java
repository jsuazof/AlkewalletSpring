package com.alkewallet6.service;

import com.alkewallet6.model.UserEntity;

import java.util.Optional;

public interface IUserService {

    Optional<UserEntity> findByUserId(int id);
    UserEntity findByUser(String email, String password);
    UserEntity findByEmail(String email);
    UserEntity saveUser(UserEntity userEntity);
}
