package com.mb.rest.webservices.limitsservice.controller;

import com.mb.rest.webservices.limitsservice.bean.Limits;
import com.mb.rest.webservices.limitsservice.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @Autowired private Configuration configuration;

  @GetMapping("/limits")
  public Limits retrieveLimits() {
    return new Limits(configuration.getMinimum(), configuration.getMaximum());
  }
}
