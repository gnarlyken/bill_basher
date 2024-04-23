package com.billbasher.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
@Entity
public class EventDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventId")
    private Long eventId;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserDAO userId;
    private Boolean eventActive;
    private String eventName;
    private LocalDateTime eventCreated = LocalDateTime.now();
}