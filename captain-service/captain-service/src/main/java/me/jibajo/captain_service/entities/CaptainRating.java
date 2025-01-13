package me.jibajo.captain_service.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class CaptainRating {
    @Id
    @GeneratedValue
    private Long ratingId;

    @ManyToOne
    @JoinColumn(name = "captain_id")
    private Captain captain;

//    @ManyToOne
//    @JoinColumn(name = "rider_id")
//    private Rider rider;

    private Integer rating;
    private String feedback;
}
