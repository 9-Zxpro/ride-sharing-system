package me.jibajo.captain_service.services;

import me.jibajo.captain_service.entities.CaptainRating;
import me.jibajo.captain_service.repositories.CaptainRatingRepository;

import java.util.List;

public class CaptainRatingServiceImp implements ICaptainRatingService {

    private final CaptainRatingRepository ratingRepository;

    public CaptainRatingServiceImp(CaptainRatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public List<CaptainRating> getRatingsByCaptain(Long captainId) {
        return ratingRepository.findByCaptain_CaptainId(captainId);
    }

    public CaptainRating addRating(CaptainRating rating) {
        return ratingRepository.save(rating);
    }
}
