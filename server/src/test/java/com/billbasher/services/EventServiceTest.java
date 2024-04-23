package com.billbasher.services;

import com.billbasher.dto.EventDTO;
import com.billbasher.model.EventDAO;
import com.billbasher.model.UserDAO;
import com.billbasher.model.UserEventDAO;
import com.billbasher.repository.EventRep;
import com.billbasher.repository.UserEventRep;
import com.billbasher.repository.UserRep;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRep eventRepository;

    @Mock
    private UserRep userRepository;

    @Mock
    private UserEventRep userEventRepository;

    @Mock
    private UserEventService userEventService;

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindEventById() {
        Long eventId = 1L;
        EventDAO event = new EventDAO();
        event.setEventId(eventId);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        EventDAO foundEvent = eventService.findEventById(eventId);
        assertEquals(event, foundEvent);
    }

    @Test
    void testUpdateEventById() {
        Long eventId = 1L;
        EventDAO existingEvent = new EventDAO();
        existingEvent.setEventId(eventId);
        existingEvent.setEventName("Old Event Name");
        EventDAO updatedEvent = new EventDAO();
        updatedEvent.setEventName("New Event Name");
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(existingEvent)).thenReturn(updatedEvent);
        EventDAO result = eventService.updateEventById(eventId, updatedEvent);
        assertEquals(updatedEvent.getEventName(), result.getEventName());
    }

    @Test
    void testGetAllEvents() {
        List<EventDAO> events = new ArrayList<>();
        events.add(new EventDAO());
        events.add(new EventDAO());
        when(eventRepository.findAll()).thenReturn(events);
        List<EventDAO> result = eventService.getAllEvents();
        assertEquals(events.size(), result.size());
    }

    @Test
    void testCreateEvent() {
        EventDAO event = new EventDAO();
        when(eventRepository.save(any(EventDAO.class))).thenReturn(event);
        UserDAO user = new UserDAO();
        user.setUserId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        EventDAO createdEvent = eventService.createEvent(event);
        assertNotNull(createdEvent);
        verify(userEventService, times(1)).addUserToEvent(any(UserEventDAO.class));
    }

    @Test
    void testGetEventsByUserId() {
        Long userId = 1L;
        UserDAO user = new UserDAO();
        user.setUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        List<EventDAO> events = new ArrayList<>();
        events.add(new EventDAO());
        events.add(new EventDAO());
        when(eventRepository.findByUserId(user)).thenReturn(events);
        List<EventDTO> result = eventService.getEventsByUserId(userId);
        assertEquals(events.size(), result.size());
    }

    @Test
    public void testDeleteEventById() {
        Long eventId = 1L;
        EventDAO event = new EventDAO();
        event.setEventId(eventId);
        event.setEventActive(false);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userEventRepository.countByEventId(event)).thenReturn(1L);
        eventService.deleteEventById(eventId);
        verify(eventRepository, times(1)).delete(event);
    }

    @Test
    public void testDeleteEventByIdNotFinished() {
        Long eventId = 1L;
        EventDAO event = new EventDAO();
        event.setEventId(eventId);
        event.setEventActive(true);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            eventService.deleteEventById(eventId);
        });
        assertEquals("Event cannot be deleted as it is not finished.", exception.getMessage());
        verify(eventRepository, never()).delete(event);
        verify(userEventService, never()).findUsersByEventId(anyLong());
        verify(userEventService, never()).removeUserFromEvent(anyLong(), anyLong());
        verify(expenseService, never()).deleteExpensesByEvent(any());
    }

    @Test
    public void testGetEventsByUserIdUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            eventService.getEventsByUserId(userId);
        });
        assertEquals("User with ID " + userId + " not found.", exception.getMessage());
    }

    @Test
    public void testDeleteEventByIdEventNotFound() {
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            eventService.deleteEventById(eventId);
        });
        verify(eventRepository, never()).delete(any(EventDAO.class));
    }

    @Test
    public void testDeactivateEvent() {
        Long eventId = 1L;
        EventDAO event = new EventDAO();
        event.setEventId(eventId);
        event.setEventActive(true);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);
        EventDAO deactivatedEvent = eventService.deactivateEvent(eventId);
        assertFalse(deactivatedEvent.getEventActive());
    }

    @Test
    public void testDeactivateEventNotFound() {
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            eventService.deactivateEvent(eventId);
        });
        assertEquals("Event with id " + eventId + " not found", exception.getMessage());
    }
}