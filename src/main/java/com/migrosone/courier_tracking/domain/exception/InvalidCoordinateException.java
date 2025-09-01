package com.migrosone.courier_tracking.domain.exception;

import java.util.Locale;

public class InvalidCoordinateException extends RuntimeException {
    public InvalidCoordinateException(double latitude, double longitude) {
        super(String.format(Locale.US, "Invalid coordinates provided: latitude=%.6f, longitude=%.6f. " +
                "Latitude must be between -90 and 90, longitude must be between -180 and 180.", 
                latitude, longitude));
    }
}