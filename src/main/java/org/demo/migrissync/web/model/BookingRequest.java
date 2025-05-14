package org.demo.migrissync.web.model;

import java.time.LocalDate;

public record BookingRequest(
  String email,
  String serviceKey,
  String serviceDescription,
  String institutionKey,
  String institutionDescription,
  LocalDate bookingDate
) {
}
