package no.kristiania.pgr301.exam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/ping")
public class PingController {

  @GetMapping(produces = "text/html")
  public ResponseEntity ping() {
    return ResponseEntity.ok("pong");
  }
}
