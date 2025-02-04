package me.jibajo.ride_matching_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jibajo.ride_matching_service.enums.MatchStatus;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RideMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    @Column(nullable = false)
    private Long rideId;

    @Column(nullable = false)
    private Long captainId;

    private Instant matchedAt;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

}
