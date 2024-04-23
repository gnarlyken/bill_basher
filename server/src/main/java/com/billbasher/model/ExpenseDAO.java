package com.billbasher.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expenses")
@Entity
public class ExpenseDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;
    @ManyToOne
    @JoinColumn(name = "eventId", referencedColumnName = "eventId")
    private EventDAO eventId;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserDAO userId;
    private String expenseReason;
    private Double amountSpent;
    private LocalDateTime expenseCreated = LocalDateTime.now();
}