package com.alkewallet6.repository;

import com.alkewallet6.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <UserEntity, Integer> {

    //userentity se usa nativequery=false usa lenguaje jpql
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND  u.password = :password")
    public UserEntity findByUser(String email, String password);

    //es con navtiquery
    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    public UserEntity findByEmail(String email);
}
