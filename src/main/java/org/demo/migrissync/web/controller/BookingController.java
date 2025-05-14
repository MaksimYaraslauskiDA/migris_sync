package org.demo.migrissync.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.migrissync.dto.BookingModel;
import org.demo.migrissync.dto.Subscriber;
import org.demo.migrissync.service.BookingService;
import org.demo.migrissync.service.TimeslotService;
import org.demo.migrissync.web.model.BookingRequest;
import org.demo.migrissync.web.model.BookingResponse;
import org.demo.migrissync.web.model.BookingUpdateRequest;
import org.demo.migrissync.web.model.NearestDateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

  private final BookingService bookingService;
  private final TimeslotService timeslotService;

  @GetMapping("/subscribers")
  public Collection<Subscriber> getSubscribers() {
    return bookingService.getAllSubscribers();
  }

  @GetMapping("/subscribers/{email}")
  public Collection<BookingModel> getBookingsById(@PathVariable String email) {
    return bookingService.getServices(email);
  }

  @PostMapping("/subscribers")
  @ResponseStatus(HttpStatus.CREATED)
  public @ResponseBody BookingResponse requestBooking(@RequestBody BookingRequest bookingRequest) {
    return new BookingResponse(bookingService.add(bookingRequest));
  }

  @PutMapping("/subscribers")
  public void updateBooking(@RequestBody BookingUpdateRequest bookingUpdateRequest) {
    bookingService.update(bookingUpdateRequest);
  }

  @DeleteMapping("/subscribers/{id}")
  public void deleteBooking(@PathVariable UUID id) {
    bookingService.remove(id);
  }

  @DeleteMapping("/subscribers/batch/{email}")
  public void deleteALLBookingForSubscriber(@PathVariable String email) {
    bookingService.removeAll(email);
  }

  @DeleteMapping("/subscribers/batch")
  public void deleteAllBookings() {
    bookingService.clear();
  }

  @GetMapping("/info")
  public @ResponseBody Map<String, String> getServiceTypes() {
    try {
      return timeslotService.listService();
    } catch (Exception e) {
      log.error("Error while getting booking info", e);
      return new HashMap<>();
    }
  }

  @GetMapping("/info/{institutionKey}")
  public @ResponseBody Map<String, String> getInstitutions(@PathVariable String institutionKey) {
    try {
      return timeslotService.listInstitutions(institutionKey);
    } catch (Exception e) {
      log.error("Error while getting institution info", e);
      return new HashMap<>();
    }
  }
  @GetMapping("/info/{serviceKey}/{institutionKey}")
  public @ResponseBody NearestDateResponse getInstitutions(
    @PathVariable String serviceKey,
    @PathVariable String institutionKey
  ) {
    try {
      return new NearestDateResponse(timeslotService.getNearestDate(serviceKey, institutionKey));
    } catch (Exception e) {
      log.error("Error while getting institution info", e);
      return new NearestDateResponse(LocalDate.MAX);
    }
  }
}
