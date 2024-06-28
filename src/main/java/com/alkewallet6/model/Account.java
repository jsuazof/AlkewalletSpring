package com.alkewallet6.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int account_id;

    @Column(name = "balance")
    private double balance;

    @Column(name = "balance_foreign")
    private double balanceForeign;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id", nullable = false)
    private UserEntity userEntity;
}
