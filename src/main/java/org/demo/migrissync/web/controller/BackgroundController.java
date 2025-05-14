package org.demo.migrissync.web.controller;

import lombok.RequiredArgsConstructor;
import org.demo.migrissync.service.BackgroundService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/background")
@RequiredArgsConstructor
public class BackgroundController {

  private final BackgroundService backgroundService;

  @PostMapping("/start")
  public void startBackground() {
    backgroundService.startBackground();
  }

  @PostMapping("/stop")
  public void stopBackground() {
    backgroundService.stopBackground();
  }
}
