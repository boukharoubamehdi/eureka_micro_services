package com.mb.rest.webservices.currencyexchangeservice.currencyexchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

// We'll create all the components in the same package.
// Ideally, if you have a large project, you would want to create separate packages for controllers,
// for beans, and everything,
// but we'll keep things simple and create everything in the same package.
@RestController
public class Controller {

  private Logger logger = LoggerFactory.getLogger(Controller.class);
  @Autowired CurrencyExchangeRepository currencyExchangeRepository;
  @Autowired private Environment environment;

  @GetMapping("/currency-exchange/from/{from}/to/{to}")
  public CurrencyExchange retrieveExchangeValue(
      @PathVariable String from, @PathVariable String to) {

    logger.info("retrieveExchangeValue called with from {} to {}", from, to);
    CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);

    if (currencyExchange == null) {
      throw new RuntimeException("Unable to find data for " + from + "to " + to);
    }
    String port = environment.getProperty("local.server.port");
    currencyExchange.setEnvironment(port);

    return currencyExchange;
  }
}
