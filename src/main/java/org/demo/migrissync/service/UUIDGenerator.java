package org.demo.migrissync.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDGenerator {
  public String generateUUID() {
    return UUID.randomUUID().toString();
  }
}
