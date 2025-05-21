package org.demo.migrissync.dto;

import org.demo.migrissync.dao.entity.Booking;

import java.time.LocalDate;

public record BookingModel(
  String id,
  String email,
  String serviceKey,
  String serviceDescription,
  String institutionKey,
  String institutionDescription,
  LocalDate subscribedAt
) {
  public BookingModel(Booking booking) {
    this(booking.getId(),
      booking.getEmail(),
      booking.getServiceKey(),
      booking.getServiceDescription(),
      booking.getInstitutionKey(),
      booking.getInstitutionDescription(),
      booking.getSubscribedAt());
  }
}
