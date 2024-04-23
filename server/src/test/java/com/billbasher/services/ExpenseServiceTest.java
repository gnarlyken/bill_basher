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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExpenseServiceTest {

    @Mock
    private ExpenseRep expenseRepository;

    @Mock
    private UserRep userRepository;

    @Mock
    private EventRep eventRepository;

    @Mock
    private UserEventRep userEventRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    public void testCreateExpense() {
        UserDAO user = new UserDAO();
        user.setUserId(1L);
        EventDAO event = new EventDAO();
        event.setEventId(1L);
        event.setUserId(user);
        UserEventDAO userEvent = new UserEventDAO();
        userEvent.setId(1L);
        userEvent.setTotal(0);
        userEvent.setUserId(user);
        userEvent.setEventId(event);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(eventRepository.save(event)).thenReturn(event);
        Mockito.when(userEventRepository.save(userEvent)).thenReturn(userEvent);

        ExpenseDAO expense = new ExpenseDAO();
        expense.setUserId(user);
        expense.setEventId(event);
        expense.setAmountSpent(9D);

        Mockito.when(expenseRepository.save(expense)).thenReturn(expense);
        expenseService.createExpense(expense);

        Mockito.verify(expenseRepository, Mockito.times(1)).save(expense);
    }

    @Test
    public void testRemoveExpenseById() {
        Long id = 1L;
        expenseService.removeExpenseById(id);
        verify(expenseRepository, times(1)).deleteById(id);
    }

    @Test
    public void testFindExpensesByEventId() {
        EventDAO event = new EventDAO();
        event.setEventId(1L);
        List<ExpenseDAO> expenses = new ArrayList<>();
        expenses.add(new ExpenseDAO());
        expenses.add(new ExpenseDAO());
        when(expenseRepository.findByEventId(event)).thenReturn(expenses);
        List<ExpenseDAO> result = expenseService.findExpensesByEventId(event);
        assertEquals(expenses.size(), result.size());
        verify(expenseRepository, times(1)).findByEventId(event);
    }

    @Test
    public void testUpdateExpenseById() {
        Long expenseId = 1L;
        ExpenseDAO expense = new ExpenseDAO();
        expense.setExpenseId(expenseId);
        when(expenseRepository.save(expense)).thenReturn(expense);
        ExpenseDAO updatedExpense = expenseService.updateExpenseById(expenseId, expense);
        assertNotNull(updatedExpense);
        assertEquals(expenseId, updatedExpense.getExpenseId());
        verify(expenseRepository, times(1)).save(expense);
    }

    @Test
    public void testUpdateExpenseByIdInvalidId() {
        Long expenseId = null;
        ExpenseDAO expense = new ExpenseDAO();
        assertThrows(IllegalArgumentException.class, () -> {
            expenseService.updateExpenseById(expenseId, expense);
        });
    }

    @Test
    public void testUpdateExpenseByIdInvalidExpense() {
        Long expenseId = 1L;
        ExpenseDAO expense = null;
        assertThrows(IllegalArgumentException.class, () -> {
            expenseService.updateExpenseById(expenseId, expense);
        });
    }

    @Test
    public void testGetExpensesByUserIdAndEventIdNonExistUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            expenseService.getExpensesByUserIdAndEventId(1L, 1L);
        });
        verify(expenseRepository, never()).findByUserIdAndEventId(any(UserDAO.class), any(EventDAO.class));
    }

    @Test
    public void testGetExpensesByUserIdAndEventIdNonExist() {
        UserDAO user = new UserDAO();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            expenseService.getExpensesByUserIdAndEventId(1L, 1L);
        });
        verify(expenseRepository, never()).findByUserIdAndEventId(any(UserDAO.class), any(EventDAO.class));
    }

    @Test
    public void testDeleteExpensesByEvent() {
        EventDAO event = new EventDAO();
        event.setEventId(1L);
        List<ExpenseDAO> expenses = new ArrayList<>();
        expenses.add(new ExpenseDAO());
        expenses.add(new ExpenseDAO());
        Mockito.when(expenseRepository.findByEventId(event)).thenReturn(expenses);
        expenseService.deleteExpensesByEvent(event);
        Mockito.verify(expenseRepository, Mockito.times(1)).deleteAll(expenses);
    }

    @Test
    public void testFindExpenseById() {
        Long expenseId = 1L;
        ExpenseDAO expense = new ExpenseDAO();
        expense.setExpenseId(expenseId);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));
        ExpenseDAO foundExpense = expenseService.findExpenseById(expenseId);
        assertEquals(expense, foundExpense);
    }

    @Test
    public void testFindExpenseByIdNotFound() {
        Long expenseId = 1L;
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            expenseService.findExpenseById(expenseId);
        });
        assertEquals("Expense with id " + expenseId + " not found", exception.getMessage());
    }

    @Test
    public void testGetAllExpenses() {
        List<ExpenseDAO> expenses = new ArrayList<>();
        ExpenseDAO expense1 = new ExpenseDAO();
        expense1.setExpenseId(1L);
        expense1.setAmountSpent(10.0);
        expenses.add(expense1);
        ExpenseDAO expense2 = new ExpenseDAO();
        expense2.setExpenseId(2L);
        expense2.setAmountSpent(20.0);
        expenses.add(expense2);
        Mockito.when(expenseRepository.findAll()).thenReturn(expenses);
        List<ExpenseDAO> retrievedExpenses = expenseService.getAllExpenses();
        assertEquals(expenses.size(), retrievedExpenses.size());
        assertEquals(expenses, retrievedExpenses);
    }

    @Test
    public void testCalculateAndUpdateTotal() {
        UserDAO user1 = new UserDAO();
        user1.setUserId(1L);
        UserDAO user2 = new UserDAO();
        user2.setUserId(2L);
        EventDAO event = new EventDAO();
        event.setEventId(1L);

        List<UserEventDAO> userEvents = new ArrayList<>();
        UserEventDAO userEvent1 = new UserEventDAO();
        userEvent1.setId(1L);
        userEvent1.setUserId(user1);
        userEvent1.setEventId(event);
        userEvent1.setTotal(0);
        userEvents.add(userEvent1);

        UserEventDAO userEvent2 = new UserEventDAO();
        userEvent2.setId(2L);
        userEvent2.setUserId(user2);
        userEvent2.setEventId(event);
        userEvent2.setTotal(0);
        userEvents.add(userEvent2);

        ExpenseDAO expense = new ExpenseDAO();
        expense.setExpenseId(1L);
        expense.setEventId(event);
        expense.setUserId(user1);
        expense.setAmountSpent(100.0);

        when(userEventRepository.findByEventId_EventId(event.getEventId())).thenReturn(userEvents);
        expenseService.calculateAndUpdateTotal(expense);

        assertEquals(50.0, userEvent1.getTotal(), 0.01);
        assertEquals(-50.0, userEvent2.getTotal(), 0.01);
    }

    @Test
    public void testGetExpensesByUserIdAndEventId() {
        Long userId = 1L;
        Long eventId = 1L;
        UserDAO user = new UserDAO();
        user.setUserId(userId);
        EventDAO event = new EventDAO();
        event.setEventId(eventId);

        List<ExpenseDAO> expenses = new ArrayList<>();
        ExpenseDAO expense1 = new ExpenseDAO();
        expense1.setExpenseId(1L);
        expense1.setEventId(event);
        expense1.setUserId(user);
        expense1.setExpenseReason("Reason 1");
        expense1.setAmountSpent(10.0);
        expense1.setExpenseCreated(LocalDateTime.now());
        expenses.add(expense1);

        ExpenseDAO expense2 = new ExpenseDAO();
        expense2.setExpenseId(2L);
        expense2.setEventId(event);
        expense2.setUserId(user);
        expense2.setExpenseReason("Reason 2");
        expense2.setAmountSpent(20.0);
        expense2.setExpenseCreated(LocalDateTime.now());
        expenses.add(expense2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(expenseRepository.findByUserIdAndEventId(user, event)).thenReturn(expenses);
        List<ExpenseDTO> expenseDTOs = expenseService.getExpensesByUserIdAndEventId(userId, eventId);

        assertEquals(expenses.size(), expenseDTOs.size());
        assertEquals(expense1.getExpenseId(), expenseDTOs.get(0).getExpenseId());
        assertEquals(expense2.getExpenseId(), expenseDTOs.get(1).getExpenseId());
        assertEquals(expense1.getEventId().getEventId(), expenseDTOs.get(0).getEventId());
        assertEquals(expense2.getEventId().getEventId(), expenseDTOs.get(1).getEventId());
        assertEquals(expense1.getExpenseReason(), expenseDTOs.get(0).getExpenseReason());
        assertEquals(expense2.getExpenseReason(), expenseDTOs.get(1).getExpenseReason());
        assertEquals(expense1.getAmountSpent(), expenseDTOs.get(0).getAmountSpent());
        assertEquals(expense2.getAmountSpent(), expenseDTOs.get(1).getAmountSpent());
        assertEquals(expense1.getExpenseCreated(), expenseDTOs.get(0).getExpenseCreated());
        assertEquals(expense2.getExpenseCreated(), expenseDTOs.get(1).getExpenseCreated());
    }
}