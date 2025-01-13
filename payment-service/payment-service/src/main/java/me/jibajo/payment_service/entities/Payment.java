package me.jibajo.payment_service.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long paymentId;

    @Column(nullable = false)
    private Long rideId;

    @Column(nullable = false)
    private Long riderId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String paymentMethod; // e.g., CARD, UPI, WALLET

    private Instant paymentTime;

    private String status; // e.g., SUCCESS, FAILURE, PENDING

    // Getters and Setters
}
