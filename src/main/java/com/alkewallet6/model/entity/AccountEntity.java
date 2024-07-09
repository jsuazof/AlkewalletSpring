package com.alkewallet6.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private double balance;
    @Column(columnDefinition = "DECIMAL(10,2)")
    private double foreignBalance;
    @Column(columnDefinition = "DECIMAL(10,2)")
    private double totalIncomingBalance;
    @Column(columnDefinition = "DECIMAL(10,2)")
    private double totalOutgoingBalance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Override
    public String toString() {
        return "balance = $" + balance;
    }
}
