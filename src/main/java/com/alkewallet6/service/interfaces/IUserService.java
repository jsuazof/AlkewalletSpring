package com.alkewallet6.service.interfaces;


import com.alkewallet6.model.DTO.UserDTO;
import com.alkewallet6.model.entity.UserEntity;

import java.util.List;

public interface IUserService {

    UserEntity createUser(UserDTO userDTO);
    UserEntity getById(Long id);
    UserEntity getUserByUsername(String username);
    UserEntity loginUser(String username,String password);
    List<UserEntity> getAllUserList();
    UserEntity getReceiverAccount(String username,String email);
    UserEntity getByEmail(String email);
}
