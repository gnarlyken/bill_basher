package com.billbasher.controllers;

import com.billbasher.dto.ExpenseDTO;
import com.billbasher.model.EventDAO;
import com.billbasher.model.ExpenseDAO;
import com.billbasher.services.EventService;
import com.billbasher.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private EventService eventService;

    @PostMapping("/api/v1/expenses")
    public ResponseEntity<ExpenseDAO> createExpense(@RequestBody ExpenseDAO expense) {
        ExpenseDAO createdExpense = expenseService.createExpense(expense);
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/expenses/{id}")
    public ResponseEntity<ExpenseDAO> findExpenseById(@PathVariable("id") Long id) {
        ExpenseDAO expense = expenseService.findExpenseById(id);
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    @PutMapping("/api/v1/expenses/{id}")
    public ResponseEntity<ExpenseDAO> updateExpenseById(@PathVariable("id") Long id, @RequestBody ExpenseDAO expense) {
        ExpenseDAO updateExpense = expenseService.updateExpenseById(id, expense);
        return new ResponseEntity<>(updateExpense, HttpStatus.OK);
    }

    @GetMapping("/api/v1/expenses/event/{id}")
    public ResponseEntity<List<ExpenseDAO>> findExpensesByEventId(@PathVariable Long id) {
        EventDAO event = eventService.findEventById(id);
        List<ExpenseDAO> expenses = expenseService.findExpensesByEventId(event);
        if (expenses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/expenses/remove/{id}")
    public ResponseEntity<String> removeExpenseById(@PathVariable("id") Long id) {
        try {
            expenseService.removeExpenseById(id);
            return ResponseEntity.ok("Expense successfully removed from event.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while removing Expense.");
        }
    }

    @GetMapping("/api/v1/expenses/{userId}/{eventId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByUserIdAndEventId(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId) {

        List<ExpenseDTO> expenseDTOs = expenseService.getExpensesByUserIdAndEventId(userId, eventId);
        return new ResponseEntity<>(expenseDTOs, HttpStatus.OK);
    }

    @GetMapping("/api/v1/expenses")
    public ResponseEntity<List<ExpenseDAO>> getAllExpenses() {
        List<ExpenseDAO> expenses = expenseService.getAllExpenses();
        if (expenses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }
}
