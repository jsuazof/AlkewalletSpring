package com.alkewallet6.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountEntity> accountList = new ArrayList<>();
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactEntity>  contactList= new ArrayList<>();
    @OneToMany(mappedBy = "userSender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity>  transactionSenderList= new ArrayList<>();
    @OneToMany(mappedBy = "userReceiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity>  transactionReceiverList= new ArrayList<>();

    public UserEntity(Long id, String name, String username, String email, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return name;
    }
}
