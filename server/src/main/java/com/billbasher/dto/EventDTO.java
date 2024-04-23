package com.billbasher.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDTO {
    private Long eventId;
    private String eventName;
    private Boolean eventActive;

    public EventDTO(Long eventId, String eventName, Boolean eventActive) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventActive = eventActive;
    }
}