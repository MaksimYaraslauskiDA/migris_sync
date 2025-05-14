package org.demo.migrissync.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.migrissync.dao.BookingRepository;
import org.demo.migrissync.dao.entity.Booking;
import org.demo.migrissync.dto.BookingModel;
import org.demo.migrissync.dto.Subscriber;
import org.demo.migrissync.web.model.BookingRequest;
import org.demo.migrissync.web.model.BookingUpdateRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {

  private final BookingRepository bookingRepository;
  private final UUIDGenerator uuidGenerator;

  public void clear() {
    log.info("Clearing storage");
    bookingRepository.deleteAll();
  }

  public Collection<BookingModel> getAllBookings() {
    log.info("Getting all bookings");
    return bookingRepository.findAll().stream().map(BookingModel::new).collect(Collectors.toList());
  }

  public Collection<Subscriber> getAllSubscribers() {
    log.info("Getting all subscribers");
    return bookingRepository.findAll().stream()
      .map(Booking::getEmail)
      .map(Subscriber::new)
      .collect(Collectors.toSet()); //Reduce collection to unique subscribers
  }

  public Collection<BookingModel> getServices(String email) {
    log.info("Getting services for subscriber {}", email);
    return bookingRepository.findAllByEmail(email).stream()
      .map(BookingModel::new)
      .collect(Collectors.toSet());
  }

  public UUID add(BookingRequest bookingRequest) {
    Booking booking = bookingRepository.save(new Booking(
      uuidGenerator.generateUUID(),
      bookingRequest.bookingDate(),
      LocalDateTime.now(),
      LocalDateTime.now(),
      bookingRequest.email(),
      bookingRequest.serviceKey(),
      bookingRequest.serviceDescription(),
      bookingRequest.institutionKey(),
      bookingRequest.institutionDescription()
    ));
    return booking.getId();
  }

  public void update(BookingUpdateRequest bookingUpdateRequest) {
    log.info("Updating booking {}", bookingUpdateRequest.id());
    bookingRepository.findById(bookingUpdateRequest.id())
      .ifPresentOrElse(booking -> {
          booking.setSubscribedAt(bookingUpdateRequest.bookingDate());
          bookingRepository.save(booking);
          log.info("Updated booking {}", bookingUpdateRequest.id());
        }, () -> log.warn("Booking with id {} not found", bookingUpdateRequest.id())
      );
  }

  public void remove(UUID id) {
    bookingRepository.findById(id)
      .ifPresentOrElse(booking -> {
        bookingRepository.deleteById(booking.getId());
        log.info("Removed service {} subscription for {}", booking.getServiceKey(), booking.getEmail());
      }, () -> log.warn("Booking with id " + id + " not found"));
  }

  public void removeByIds(List<UUID> ids) {
    log.info("Removing services for booking {}", ids);
    bookingRepository.deleteAllById(ids);
  }

  public void removeAll(String email) {
    log.info("Removing all subscriptions for {}", email);
    Collection<Booking> bookingsForRemove = bookingRepository.findAllByEmail(email);
    if (bookingsForRemove.isEmpty()) {
      log.warn("No subscriptions found for {}", email);
    } else {
      bookingRepository.deleteAllById(bookingsForRemove.stream().map(Booking::getId).collect(Collectors.toList()));
      log.info("Removed all subscriptions for {}", email);
    }
  }
}
