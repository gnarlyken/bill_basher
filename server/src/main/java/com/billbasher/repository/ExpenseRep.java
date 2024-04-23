package com.billbasher.repository;

import com.billbasher.model.EventDAO;
import com.billbasher.model.ExpenseDAO;
import com.billbasher.model.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRep extends JpaRepository<ExpenseDAO, Long> {
    List<ExpenseDAO> findByEventId(EventDAO event);
    List<ExpenseDAO> findByUserIdAndEventId(UserDAO user, EventDAO event);
}