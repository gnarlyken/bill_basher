package com.billbasher.repository;

import com.billbasher.model.EventDAO;
import com.billbasher.model.UserEventDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserEventRep extends JpaRepository<UserEventDAO, Long> {
    Optional<UserEventDAO> findByUserId_UserIdAndEventId_EventId(Long userId, Long eventId);
    List<UserEventDAO> findByUserIdUserId(Long userId);
    long countByEventId(EventDAO event);
    List<UserEventDAO> findByEventId_EventId(Long eventId);

    @Query(value = "SELECT NEW map(ue.total AS total, u.username AS username, u.userId AS userId, e.eventId AS eventId) " +
            "FROM UserEventDAO ue " +
            "JOIN ue.userId u " +
            "JOIN ue.eventId e " +
            "WHERE e.eventId = :eventId")
    List<Map<String, Object>> getTotalUsernameUserIdEventIdByEventId(@Param("eventId") Long eventId);
}