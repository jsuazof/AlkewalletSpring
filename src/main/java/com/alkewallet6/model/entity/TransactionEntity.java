package com.alkewallet6.model.entity;

import com.alkewallet6.model.enums.TransactionStatus;
import com.alkewallet6.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private double amount;
    private String message;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @CreatedDate
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "contact_id_receiver", nullable = true)
    private ContactEntity contactReceiver;
    @ManyToOne
    @JoinColumn(name = "user_id_sender", nullable = false)
    private UserEntity userSender;
    @ManyToOne
    @JoinColumn(name = "user_id_receiver", nullable = true)
    private UserEntity userReceiver;
}
