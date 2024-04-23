package com.billbasher.services;

import com.billbasher.dto.ExpenseDTO;
import com.billbasher.model.EventDAO;
import com.billbasher.model.ExpenseDAO;
import com.billbasher.model.UserDAO;
import com.billbasher.model.UserEventDAO;
import com.billbasher.repository.EventRep;
import com.billbasher.repository.ExpenseRep;
import com.billbasher.repository.UserEventRep;
import com.billbasher.repository.UserRep;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRep expenseRepository;
    @Autowired
    private UserRep userRepository;
    @Autowired
    private EventRep eventRepository;
    @Autowired
    private UserEventRep userEventRep;

    public ExpenseDAO createExpense(ExpenseDAO expense) {
        calculateAndUpdateTotal(expense);
        return expenseRepository.save(expense);
    }

    public void calculateAndUpdateTotal(ExpenseDAO expense) {
        List<UserEventDAO> userEvents = userEventRep.findByEventId_EventId(expense.getEventId().getEventId());
        int totalParticipants = userEvents.size();
        double sharePerParticipant = expense.getAmountSpent() / totalParticipants;
        for (UserEventDAO userEvent : userEvents) {
            double userShare = 0.0;
            if (userEvent.getUserId().getUserId().equals(expense.getUserId().getUserId())) {
                userShare = sharePerParticipant * (totalParticipants - 1);
            } else {
                userShare = -sharePerParticipant;
            }
            userEvent.setTotal(userEvent.getTotal() + userShare);
            userEventRep.save(userEvent);
        }
    }

    public void removeExpenseById(@PathVariable("id") Long id) {
        expenseRepository.deleteById(id);
    }

    public ExpenseDAO findExpenseById(@PathVariable("id") Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Expense with id " + id + " not found"));
    }

    public List<ExpenseDAO> findExpensesByEventId(@NotNull @Valid EventDAO event) {
        return expenseRepository.findByEventId(event);
    }

    public ExpenseDAO updateExpenseById(Long id, @Valid ExpenseDAO expense) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid expense id");
        }
        if (expense == null) {
            throw new IllegalArgumentException("Expense object must not be null");
        }
        return expenseRepository.save(expense);
    }

    public List<ExpenseDTO> getExpensesByUserIdAndEventId(Long userId, Long eventId) {
        Optional<UserDAO> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
        UserDAO user = userOptional.get();
        Optional<EventDAO> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            throw new IllegalArgumentException("Event with ID " + eventId + " not found.");
        }
        EventDAO event = eventOptional.get();
        List<ExpenseDAO> expenses = expenseRepository.findByUserIdAndEventId(user, event);
        return expenses.stream()
                .map(expense -> {
                    ExpenseDTO expenseDTO = new ExpenseDTO();
                    expenseDTO.setExpenseId(expense.getExpenseId());
                    expenseDTO.setEventId(expense.getEventId().getEventId());
                    expenseDTO.setExpenseReason(expense.getExpenseReason());
                    expenseDTO.setAmountSpent(expense.getAmountSpent());
                    expenseDTO.setExpenseCreated(expense.getExpenseCreated());
                    return expenseDTO;
                })
                .collect(Collectors.toList());
    }

    public List<ExpenseDAO> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public void deleteExpensesByEvent(EventDAO event) {
        List<ExpenseDAO> expenses = expenseRepository.findByEventId(event);
        expenseRepository.deleteAll(expenses);
    }
}