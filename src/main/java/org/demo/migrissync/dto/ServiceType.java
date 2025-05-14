package org.demo.migrissync.dto;

import java.time.LocalDate;

public record ServiceType(
  String serviceKey,
  String serviceDescription,
  String institutionKey,
  String institutionDescription,
  LocalDate subscribedAt
) {
  public ServiceType(BookingModel bookingModel) {
    this(bookingModel.serviceKey(),
      bookingModel.serviceDescription(),
      bookingModel.institutionKey(),
      bookingModel.institutionDescription(),
      bookingModel.subscribedAt());
  }
}
