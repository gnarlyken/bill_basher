package com.billbasher.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExpenseDTO {
    private Long expenseId;
    private Long eventId;
    private String expenseReason;
    private Double amountSpent;
    private LocalDateTime expenseCreated;
}
