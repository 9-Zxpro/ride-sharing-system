package me.jibajo.captain_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "captain_earnings")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaptainEarnings{

    @Id
    @GeneratedValue
    private Long earningsId;

    @ManyToOne
    @JoinColumn(name = "captain_id")
    private Captain captain;

    private LocalDate date;
    private BigDecimal totalEarnings;
    private Long rideCount;
}
