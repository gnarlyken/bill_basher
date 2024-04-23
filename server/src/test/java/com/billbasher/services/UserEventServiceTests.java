package com.billbasher.services;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.billbasher.model.EventDAO;
import com.billbasher.model.UserDAO;
import com.billbasher.model.UserEventDAO;
import com.billbasher.repository.EventRep;
import com.billbasher.repository.UserEventRep;
import com.billbasher.repository.UserRep;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserEventServiceTests {

    @Mock
    private UserRep userRep;

    @Mock
    private EventRep eventRep;

    @Mock
    private UserEventRep userEventRep;

    @InjectMocks
    private UserEventService userEventService;

    @Test
    public void testFindUserEventsByUserId() {
        UserEventDAO userEventDAO = new UserEventDAO();
        userEventDAO.setUserId(new UserDAO());
        userEventDAO.setEventId(new EventDAO());
        when(userEventRep.findByUserIdUserId(anyLong())).thenReturn(List.of(userEventDAO));
        List<UserEventDAO> result = userEventService.findUserEventsByUserId(1L);
        assertEquals(1, result.size());
    }

    @Test
    public void testAddUserToEvent_UserAndEventExist() {
        UserDAO user = new UserDAO();
        user.setUserId(1L);
        EventDAO event = new EventDAO();
        event.setEventId(2L);

        when(userRep.findById(anyLong())).thenReturn(Optional.of(user));
        when(eventRep.findById(anyLong())).thenReturn(Optional.of(event));
        ArgumentCaptor<UserEventDAO> captor = ArgumentCaptor.forClass(UserEventDAO.class);

        UserEventDAO userEvent = new UserEventDAO();
        userEvent.setUserId(user);
        userEvent.setEventId(event);

        userEventService.addUserToEvent(userEvent);
        verify(userEventRep, times(1)).save(captor.capture());
        UserEventDAO capturedUserEvent = captor.getValue();
        assertEquals(user.getUserId(), capturedUserEvent.getUserId().getUserId());
        assertEquals(event.getEventId(), capturedUserEvent.getEventId().getEventId());
    }

    @Test
    public void testAddUserToEvent_UserNotExist() {
        UserEventDAO userEvent = new UserEventDAO();
        UserDAO user = new UserDAO();
        user.setUserId(1L);
        userEvent.setUserId(user);
        EventDAO event = new EventDAO();
        event.setEventId(1L);
        userEvent.setEventId(event);

        when(userRep.findById(anyLong())).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userEventService.addUserToEvent(userEvent));
        assertEquals("User with ID 1 not found.", exception.getMessage());
    }

    @Test
    public void testAddUserToEvent_EventNotExist() {
        UserEventDAO userEvent = new UserEventDAO();
        UserDAO user = new UserDAO();
        user.setUserId(1L);
        userEvent.setUserId(user);
        EventDAO event = new EventDAO();
        event.setEventId(1L);
        userEvent.setEventId(event);

        when(userRep.findById(anyLong())).thenReturn(Optional.of(user));
        when(eventRep.findById(anyLong())).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userEventService.addUserToEvent(userEvent));
        assertEquals("Event with ID 1 not found.", exception.getMessage());
    }

    @Test
    public void testRemoveUserFromEvent_UserEventExists() {
        Long userId = 1L;
        Long eventId = 1L;
        UserDAO user = new UserDAO();
        user.setUserId(userId);
        when(userRep.findById(userId)).thenReturn(Optional.of(user));

        EventDAO event = new EventDAO();
        event.setEventId(eventId);
        when(eventRep.findById(eventId)).thenReturn(Optional.of(event));

        UserEventDAO userEvent = new UserEventDAO();
        userEvent.setUserId(user);
        userEvent.setEventId(event);

        when(userEventRep.findByUserId_UserIdAndEventId_EventId(userId, eventId)).thenReturn(Optional.of(userEvent));
        userEventService.removeUserFromEvent(userId, eventId);
        verify(userEventRep, times(1)).delete(userEvent);
    }

    @Test
    public void testFindUsersByEventId() {
        EventDAO eventDAO = new EventDAO();
        eventDAO.setEventId(1L);
        UserDAO user1 = new UserDAO();
        user1.setUserId(1L);
        UserDAO user2 = new UserDAO();
        user2.setUserId(2L);
        UserEventDAO userEvent1 = new UserEventDAO();
        userEvent1.setUserId(user1);
        userEvent1.setEventId(eventDAO);
        UserEventDAO userEvent2 = new UserEventDAO();
        userEvent2.setUserId(user2);
        userEvent2.setEventId(eventDAO);

        when(userEventRep.findByEventId_EventId(anyLong())).thenReturn(List.of(userEvent1, userEvent2));
        List<UserDAO> result = userEventService.findUsersByEventId(1L);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(2L, result.get(1).getUserId());
    }

    @Test
    public void testGetBalanceByEventId() {
        EventDAO eventDAO = new EventDAO();
        eventDAO.setEventId(1L);
        UserDAO user1 = new UserDAO();
        user1.setUserId(1L);
        user1.setUsername("user1");
        UserDAO user2 = new UserDAO();
        user2.setUserId(2L);
        user2.setUsername("user2");
        UserEventDAO userEvent1 = new UserEventDAO();
        userEvent1.setUserId(user1);
        userEvent1.setEventId(eventDAO);
        userEvent1.setTotal(100.0);
        UserEventDAO userEvent2 = new UserEventDAO();
        userEvent2.setUserId(user2);
        userEvent2.setEventId(eventDAO);
        userEvent2.setTotal(-50.0);

        when(userEventRep.getTotalUsernameUserIdEventIdByEventId(anyLong())).thenReturn(List.of(
                Map.of("total", 100.0, "username", "user1", "userId", 1L, "eventId", 1L),
                Map.of("total", -50.0, "username", "user2", "userId", 2L, "eventId", 1L)
        ));

        List<Map<String, Object>> result = userEventService.getBalanceByEventId(1L);

        assertEquals(2, result.size());
        assertEquals(100.0, result.get(0).get("total"));
        assertEquals("user1", result.get(0).get("username"));
        assertEquals(1L, result.get(0).get("userId"));
        assertEquals(1L, result.get(0).get("eventId"));
        assertEquals(-50.0, result.get(1).get("total"));
        assertEquals("user2", result.get(1).get("username"));
        assertEquals(2L, result.get(1).get("userId"));
        assertEquals(1L, result.get(1).get("eventId"));
    }

    @Test
    public void testFindUserEventsByUserId_WhenNoUserEventsExist() {
        when(userEventRep.findByUserIdUserId(anyLong())).thenReturn(Collections.emptyList());
        List<UserEventDAO> result = userEventService.findUserEventsByUserId(1L);
        assertEquals(0, result.size());
    }
}