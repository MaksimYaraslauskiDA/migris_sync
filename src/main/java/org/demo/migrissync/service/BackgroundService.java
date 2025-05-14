package org.demo.migrissync.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.demo.migrissync.dto.BookingModel;
import org.demo.migrissync.dto.ServiceType;
import org.demo.migrissync.dto.SubscriberGroupInfo;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackgroundService {

  private volatile static boolean isRunning = false;

  private final BookingService bookingService;
  private final TimeslotService timeslotService;
  private final EmailService emailService;

  public boolean status() {
    log.info("Getting status of background service: {}", isRunning);
    return isRunning;
  }

  public void stopBackground() {
    log.info("Stopping background service");
    isRunning = false;
  }

  public void startBackground() {
    log.info("Starting background service");
    isRunning = true;
    new Thread(() -> {
      while (isRunning) {
//        Thread.onSpinWait();
        processBooking();
        try {
          Thread.currentThread().sleep(30000);
        } catch (InterruptedException e) {
          log.warn("Background service interrupted");
        }
      }
    }).start();
  }

  private void processBooking() {
    Collection<BookingModel> bookings = bookingService.getAllBookings();
    Map<ServiceType, List<SubscriberGroupInfo>> subscriptions = new HashMap<>();
    for (BookingModel booking : bookings) {
      subscriptions.computeIfAbsent(new ServiceType(booking),
        k -> new ArrayList<>()).add(new SubscriberGroupInfo(booking.id(), booking.email()));
    }

    subscriptions.keySet().forEach(serviceType -> {
      String serviceKey = serviceType.serviceKey();
      String institutionKey = serviceType.institutionKey();
      LocalDate subscribedAt = serviceType.subscribedAt();
      List<SubscriberGroupInfo> subscribers = subscriptions.get(serviceType);
      if (subscribers.isEmpty()) {
        log.warn("No subscribers for {}", serviceType);
        return;
      }
      try {
        log.info("Processing of finding booking for {}", serviceType);
        LocalDate nearestDate = timeslotService.getNearestDate(serviceKey, institutionKey);
        if (nearestDate.isBefore(subscribedAt)) {
          log.info("Nearest date: {}", nearestDate);
          String timeslots = timeslotService.getNearestTimeslots(serviceKey, institutionKey, subscribedAt)
            .stream()
            .map(LocalDateTime::toLocalTime)
            .map(LocalTime::toString)
            .collect(Collectors.joining(", "));
          String serviceDescription = serviceType.serviceDescription();
          String institutionTitle = serviceType.institutionDescription();
          log.info("Nearest timeslot: {}", timeslots);
          String message = MessageFormat.format(
            "Old timeslot: {0}\nNew dates {1}\nAvailable slots: {2}\nService: {3}\nInstitution: {4}",
            subscribedAt, nearestDate, timeslots, serviceDescription, institutionTitle);

          log.info("New timeslot: {}", message);
          emailService.sendEmail(subscribers.stream().map(SubscriberGroupInfo::email).toList(), "New timeslot: " + subscribedAt, message);
          bookingService.removeByIds(subscribers.stream().map(SubscriberGroupInfo::id).toList());
        } else {
          log.info("No new nearest date for {}", serviceType);
        }
      } catch (Exception e) {
        log.error("Error during synchronization: {}", e.getMessage(), e);
      }
    });
  }
}
