package com.billbasher.controllers;

import com.billbasher.dto.EventDTO;
import com.billbasher.dto.UserDTO;
import com.billbasher.model.EventDAO;
import com.billbasher.model.UserDAO;
import com.billbasher.model.UserEventDAO;
import com.billbasher.services.UserEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserEventController {

    private final UserEventService userEventService;

    public UserEventController(UserEventService userEventService) {
        this.userEventService = userEventService;
    }

    @PostMapping("/api/v1/user-event/add")
    public ResponseEntity<String> addUserToEvent(@RequestBody UserEventDAO userEventDAO) {
        try {
            userEventService.addUserToEvent(userEventDAO);
            return ResponseEntity.ok("User successfully added to event.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding user to event.");
        }
    }

    @DeleteMapping("/api/v1/user-event/remove/{userId}/{eventId}")
    public ResponseEntity<String> removeUserFromEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        try {
            userEventService.removeUserFromEvent(userId, eventId);
            return ResponseEntity.ok("User successfully removed from event.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while removing user from event.");
        }
    }

    @GetMapping("/api/v1/events/by-user/{userId}")
    public ResponseEntity<List<EventDTO>> getEventsByUserId(@PathVariable("userId") Long userId) {
        List<UserEventDAO> userEvents = userEventService.findUserEventsByUserId(userId);
        List<EventDTO> events = new ArrayList<>();
        for (UserEventDAO userEvent : userEvents) {
            EventDAO event = userEvent.getEventId();
            EventDTO eventDTO = new EventDTO(event.getEventId(),event.getEventName(),event.getEventActive());
            events.add(eventDTO);
        }
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/api/v1/users/by-event/{eventId}")
    public ResponseEntity<List<UserDTO>> getUsersByEventId(@PathVariable("eventId") Long eventId) {
        List<UserDAO> users = userEventService.findUsersByEventId(eventId);
        List<UserDTO> userDTOs = new ArrayList<>();
        for (UserDAO user : users) {
            UserDTO userDTO = UserDTO.mapUserDAOToDTO(user);
            userDTOs.add(userDTO);
        }
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/api/v1/balance/{eventId}")
    public List<Map<String, Object>> getTotalUsernameUserIdEventIdByEventId(@PathVariable Long eventId) {
        return userEventService.getBalanceByEventId(eventId);
    }
}