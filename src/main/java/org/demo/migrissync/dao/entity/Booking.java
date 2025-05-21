package org.demo.migrissync.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

  @Id
  private String id;

  @Column(name = "subscribed_at", nullable = false)
  private LocalDate subscribedAt;

  @Column(name = "creation_timestamp", nullable = false)
  private LocalDateTime creationTimestamp;

  @Column(name = "update_timestamp", nullable = false)
  private LocalDateTime updateTimestamp;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "service_key", nullable = false)
  private String serviceKey;

  @Column(name = "service_description", nullable = false)
  String serviceDescription;

  @Column(name = "institution_key", nullable = false)
  private String institutionKey;

  @Column(name = "institution_description", nullable = false)
  String institutionDescription;

}