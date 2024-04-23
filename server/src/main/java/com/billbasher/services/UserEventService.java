package com.billbasher.services;

import com.billbasher.model.EventDAO;
import com.billbasher.model.UserDAO;
import com.billbasher.model.UserEventDAO;
import com.billbasher.repository.EventRep;
import com.billbasher.repository.UserEventRep;
import com.billbasher.repository.UserRep;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserEventService {
    private final UserEventRep userEventRep;
    private final UserRep userRep;
    private final EventRep eventRep;

    public UserEventService(UserEventRep userEventRep, UserRep userRep, EventRep eventRep) {
        this.userEventRep = userEventRep;
        this.userRep = userRep;
        this.eventRep = eventRep;
    }

    public List<UserEventDAO> findUserEventsByUserId(Long userId) {
        return userEventRep.findByUserIdUserId(userId);
    }

    public void addUserToEvent(UserEventDAO userEventDAO) {
        Long userId = userEventDAO.getUserId().getUserId();
        Long eventId = userEventDAO.getEventId().getEventId();
        Optional<UserDAO> optionalUser = userRep.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
        UserDAO user = optionalUser.get();
        Optional<EventDAO> optionalEvent = eventRep.findById(eventId);
        if (optionalEvent.isEmpty()) {
            throw new IllegalArgumentException("Event with ID " + eventId + " not found.");
        }
        EventDAO event = optionalEvent.get();
        UserEventDAO newUserEvent = new UserEventDAO();
        newUserEvent.setUserId(user);
        newUserEvent.setEventId(event);
        userEventRep.save(newUserEvent);
    }

    public void removeUserFromEvent(Long userId, Long eventId) {
        Optional<UserDAO> optionalUser = userRep.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
        Optional<EventDAO> optionalEvent = eventRep.findById(eventId);
        if (optionalEvent.isEmpty()) {
            throw new IllegalArgumentException("Event with ID " + eventId + " not found.");
        }
        Optional<UserEventDAO> optionalUserEvent = userEventRep.findByUserId_UserIdAndEventId_EventId(userId, eventId);
        if (optionalUserEvent.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " is not participating in Event with ID " + eventId + ".");
        }
        userEventRep.delete(optionalUserEvent.get());
    }

    public List<UserDAO> findUsersByEventId(Long eventId) {
        List<UserEventDAO> userEventList = userEventRep.findByEventId_EventId(eventId);
        List<UserDAO> users = userEventList.stream()
                .map(UserEventDAO::getUserId)
                .collect(Collectors.toList());
        return users;
    }

    public List<Map<String, Object>> getBalanceByEventId(Long eventId) {
        return userEventRep.getTotalUsernameUserIdEventIdByEventId(eventId);
    }
}