package org.demo.migrissync.web.controller;

import lombok.RequiredArgsConstructor;
import org.demo.migrissync.service.BackgroundService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/background")
@RequiredArgsConstructor
public class BackgroundController {

  private final BackgroundService backgroundService;

  @GetMapping("/status")
  public Map<String, Boolean> status() {
    return Map.of("status", backgroundService.status());
  }

  @PostMapping("/start")
  public void startBackground() {
    backgroundService.startBackground();
  }

  @PostMapping("/stop")
  public void stopBackground() {
    backgroundService.stopBackground();
  }
}
