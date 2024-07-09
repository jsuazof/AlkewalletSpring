package com.alkewallet6.service;


import com.alkewallet6.model.DTO.UserDTO;
import com.alkewallet6.model.entity.UserEntity;
import com.alkewallet6.repository.UserRepository;
import com.alkewallet6.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserEntity createUser(UserDTO userDTO) {
        UserEntity userUsernameAndEmailDB = userRepository.findByNameAndEmail(userDTO.getUsername(),userDTO.getEmail());
        if (userUsernameAndEmailDB == null){
            UserEntity userEmailDB = userRepository.findByEmail(userDTO.getEmail());
            if (userEmailDB == null){
                UserEntity user = new UserEntity();
                user.setName(userDTO.getName());
                user.setUsername(userDTO.getUsername());
                user.setEmail(userDTO.getEmail());
                if (userDTO.getPassword().equals(userDTO.getPasswordConfirm())){
                    user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
                    userRepository.save(user);
                }else {
                    throw new IllegalArgumentException("Las contraseñas no coinciden");
                }
                return user;
            }else {
                throw new IllegalArgumentException("Este correo ya esta en nuestro registros");
            }
        }else {
            throw new IllegalArgumentException("Ya tienes cuenta con nosotros");
        }
    }

    @Override
    public UserEntity getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID proporcionado no debe ser nulo");
        }
        Optional<UserEntity> optional = userRepository.findById(id);
        UserEntity user;

        if (optional.isPresent()){
            user = optional.get();
        }else{
            throw new RuntimeException("Usuario no es encontrado: " + id);
        }
        return user;
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserEntity loginUser(String username,String password){
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Username Incorrecto");
        } else if (!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("Contraseña Incorrecta");
        }else{
            return user;
        }
    }

    @Override
    public List<UserEntity> getAllUserList() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity getReceiverAccount(String username, String email) {
        return userRepository.findByNameAndEmail(username,email);
    }

    @Override
    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
