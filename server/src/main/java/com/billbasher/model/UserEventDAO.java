package com.billbasher.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_event")
@Entity
public class UserEventDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private UserDAO userId;
    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "eventId")
    private EventDAO eventId;
    private double total;
}