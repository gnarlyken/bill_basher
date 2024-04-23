package com.billbasher.services;

import com.billbasher.dto.EventDTO;
import com.billbasher.model.EventDAO;
import com.billbasher.model.UserDAO;
import com.billbasher.model.UserEventDAO;
import com.billbasher.repository.EventRep;
import com.billbasher.repository.UserRep;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRep eventRepository;
    @Autowired
    private UserRep userRepository;
    @Autowired
    private UserEventService userEventService;
    @Autowired
    private ExpenseService expenseService;

    public EventDAO findEventById(@PathVariable("id") Long id) {
        Optional<EventDAO> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            return eventOptional.get();
        } else {
            throw new EntityNotFoundException("Event with id " + id + " not found");
        }
    }

    public EventDAO updateEventById(Long id, EventDAO event) {
        Optional<EventDAO> existingEventOptional = eventRepository.findById(id);
        if (existingEventOptional.isPresent()) {
            EventDAO existingEvent = existingEventOptional.get();
            existingEvent.setEventName(event.getEventName());
            existingEvent.setEventActive(event.getEventActive());
            return eventRepository.save(existingEvent);
        } else {
            throw new EntityNotFoundException("Event with id " + id + " not found");
        }
    }

    public void deleteEventById(Long id) {
        Optional<EventDAO> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            EventDAO event = eventOptional.get();
            if (!event.getEventActive()) {
                List<UserDAO> users = userEventService.findUsersByEventId(id);
                for (UserDAO user : users) {
                    userEventService.removeUserFromEvent(user.getUserId(), id);
                }
                expenseService.deleteExpensesByEvent(event);
                eventRepository.delete(event);
            } else {
                throw new IllegalStateException("Event cannot be deleted as it is not finished.");
            }
        } else {
            throw new IllegalArgumentException("Event with ID " + id + " not found.");
        }
    }

    public List<EventDAO> getAllEvents() {
        return eventRepository.findAll();
    }

    public EventDAO createEvent(EventDAO event) {
        EventDAO createdEvent = eventRepository.save(event);
        UserEventDAO userEDAO = new UserEventDAO();
        userEDAO.setEventId(createdEvent);
        userEDAO.setUserId(event.getUserId());
        userEDAO.setTotal(0);
        userEventService.addUserToEvent(userEDAO);
        return createdEvent;
    }

    public List<EventDTO> getEventsByUserId(Long userId) {
        Optional<UserDAO> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }
        UserDAO user = userOptional.get();
        List<EventDAO> events = eventRepository.findByUserId(user);
        return events.stream()
                .map(event -> new EventDTO(event.getEventId(), event.getEventName(), event.getEventActive()))
                .collect(Collectors.toList());
    }

    public EventDAO deactivateEvent(Long eventId) {
        Optional<EventDAO> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            EventDAO event = eventOptional.get();
            event.setEventActive(false);
            return eventRepository.save(event);
        } else {
            throw new EntityNotFoundException("Event with id " + eventId + " not found");
        }
    }
}