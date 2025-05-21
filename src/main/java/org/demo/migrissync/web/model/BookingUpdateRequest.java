package org.demo.migrissync.web.model;

import java.time.LocalDate;

public record BookingUpdateRequest(String id, LocalDate bookingDate) {
}
