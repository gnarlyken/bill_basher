package com.billbasher.repository;

import com.billbasher.model.EventDAO;
import com.billbasher.model.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRep extends JpaRepository<EventDAO, Long> {
    List<EventDAO> findByUserId(UserDAO user);
}