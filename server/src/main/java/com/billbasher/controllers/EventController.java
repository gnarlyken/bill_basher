package com.billbasher.controllers;

import com.billbasher.dto.EventDTO;
import com.billbasher.model.EventDAO;
import com.billbasher.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/api/v1/events/{id}")
    public ResponseEntity<EventDAO> findEventById(@PathVariable("id") Long id) {
        EventDAO event = eventService.findEventById(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @PutMapping("/api/v1/events/{id}")
    public ResponseEntity<EventDAO> updateEventById(@PathVariable("id") Long id, @RequestBody EventDAO event) {
        EventDAO updatedEvent = eventService.updateEventById(id, event);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @GetMapping("/api/v1/events")
    public ResponseEntity<List<EventDAO>> getAllEvents() {
        List<EventDAO> events = eventService.getAllEvents();
        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping("/api/v1/events")
    public ResponseEntity<EventDAO> createEvent(@RequestBody EventDAO event) {
        EventDAO createdEvent = eventService.createEvent(event);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/events/list/{userId}")
    public ResponseEntity<List<EventDTO>> getEventsByUserId(@PathVariable("userId") Long userId) {
        List<EventDTO> eventDTOs = eventService.getEventsByUserId(userId);
        return new ResponseEntity<>(eventDTOs, HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/events/{id}")
    public ResponseEntity<Void> deleteEventById(@PathVariable("id") Long id) {
        eventService.deleteEventById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/v1/events/deactivate/{id}")
    public ResponseEntity<EventDAO> deactivateEvent(@PathVariable("id") Long id) {
        EventDAO deactivatedEvent = eventService.deactivateEvent(id);
        return new ResponseEntity<>(deactivatedEvent, HttpStatus.OK);
    }
}