package me.jibajo.captain_service.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "captain_ratings")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaptainRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @ManyToOne
    @JoinColumn(name = "captain_id")
    private Captain captain;

    private Integer rating;
    private String feedback;
}
