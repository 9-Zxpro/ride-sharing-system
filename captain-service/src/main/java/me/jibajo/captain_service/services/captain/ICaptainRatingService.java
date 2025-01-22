package me.jibajo.captain_service.services.captain;

import me.jibajo.captain_service.entities.CaptainRating;

import java.util.List;

public interface ICaptainRatingService {
     List<CaptainRating> getRatingsByCaptain(Long captainId);

     CaptainRating addRating(CaptainRating rating);
}
