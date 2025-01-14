package me.jibajo.captain_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "captain_earnings")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaptainEarnings{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long earningsId;

    @ManyToOne
    @JoinColumn(name = "captain_id")
    private Captain captain;

    private LocalDate date;
    private BigDecimal totalEarnings;
    private Long rideCount;
}
