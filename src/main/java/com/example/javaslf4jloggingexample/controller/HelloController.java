package com.example.javaslf4jloggingexample.controller;

import com.example.javaslf4jloggingexample.Service.HelloService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class HelloController {
  private HelloService helloService;

  @GetMapping("/hello")
  public String hello() {
    log.info("Call GET /hello endpoint ... ");
    return helloService.hello();
  }
}
