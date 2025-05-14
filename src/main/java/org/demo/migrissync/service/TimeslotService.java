package org.demo.migrissync.service;

import lombok.RequiredArgsConstructor;
import org.demo.migrissync.web.model.Institution;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeslotService {

  private final MigrisApiService migrisApiService;

  public Map<String, String> listService() throws Exception {
    return migrisApiService.getServiceDescription()
      .visits()
      .reserve()
      .serviceDescription();
  }

  public Map<String, String> listInstitutions(String serviceKey) throws Exception {
    return migrisApiService.getInstitutions(serviceKey).stream()
      .collect(Collectors.toMap(Institution::getKey, Institution::getTitleEn));
  }

  public LocalDate getNearestDate(String serviceKey, String institutionKey) throws Exception {
    return migrisApiService.getDateSlots(serviceKey, institutionKey).stream()
      .sorted()
      .map(LocalDateTime::toLocalDate)
      .findFirst()
      .orElseThrow();
  }

  public List<LocalDateTime> getNearestTimeslots(String serviceKey, String institutionKey, LocalDate date) throws Exception {
    return migrisApiService.getTimeSlots(serviceKey, institutionKey, date).stream()
      .sorted()
      .toList();
  }
}
