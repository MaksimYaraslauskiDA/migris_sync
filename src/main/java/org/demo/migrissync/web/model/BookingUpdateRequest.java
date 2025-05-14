package org.demo.migrissync.web.model;

import java.time.LocalDate;
import java.util.UUID;

public record BookingUpdateRequest(UUID id, LocalDate bookingDate) {
}
