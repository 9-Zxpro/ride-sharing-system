package me.jibajo.ride_management_service.enums;

import java.util.Map;
import java.util.Set;

public enum RideStatus {
    REQUESTED,
    DRIVER_ASSIGNED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    private static final Map<RideStatus, Set<RideStatus>> ALLOWED_TRANSITIONS = Map.of(
            REQUESTED, Set.of(DRIVER_ASSIGNED, CANCELLED),
            DRIVER_ASSIGNED, Set.of(IN_PROGRESS, CANCELLED),
            IN_PROGRESS, Set.of(COMPLETED, CANCELLED),
            COMPLETED, Set.of(),
            CANCELLED, Set.of()
    );

    public boolean isAllowedTransitionFrom(RideStatus currentStatus) {
        return ALLOWED_TRANSITIONS.get(currentStatus).contains(this);
    }
}
