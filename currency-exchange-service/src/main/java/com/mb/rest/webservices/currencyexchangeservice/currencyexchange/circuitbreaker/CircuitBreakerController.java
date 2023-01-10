package com.mb.rest.webservices.currencyexchangeservice.currencyexchange.circuitbreaker;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {

  // In this class we explored some important features that are provided by mainly a Circuit
  // Breaker framework called resilience4j.
  // We looked at Retry, CircuitBreaker, RateLimiter, and Bulkhead features.
  private final Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

  @GetMapping("/sample-api-retry")
  // we will use the default configuration for the retry.
  // by default, @Retry, what it does is if there is any failure, if there is any failure in the
  // execution of this specific method.
  // So, in this method, when it's executing, if there's an exception, what would happen is, it
  // would be  retried thrice.
  // and if the retry fails all the three times, only then it would return an error back.
  // @Retry(name = "default")
  @Retry(name = "sample-api", fallbackMethod = "hardCodedResponse")
  public String sampleApiRetryExample() {
    // let's make this fail and then, let's focus on retry.
    // So, what I would do is, I would actually create a new RestTemplate and let's just call some
    // dummy API.

    logger.info("Sample Api retry received call");
    ResponseEntity<String> forEntity =
        new RestTemplate().getForEntity("http://localhost:8000/some-dummy-url", String.class);
    return forEntity.getBody();
  }

  // We want to send the request every, I want to be able to send 10 requests per second.
  // watch -n 0.1 curl http://localhost:8000/sample-api-circuit-breaker

  // we'd see that there are requests going on (when we hit the
  // "http://localhost:8000/sample-api-circuit-breaker" 10 times).
  // However, we'd see that there are no method calls in the logging console.
  // So, what is happening in here?
  // The CircuitBreaker is returning the response back without even calling this method.
  // we can see that there is no log being generated in the logging console.
  // That means the method is not being called, directly
  // the fallback method is being called and the response is returned.

  // We can see that there are 10 requests fired every second, and that is what the CircuitBreaker
  // framework allows us to do.
  // If a microservice (X call Y) and Y is down, then what does the CircuitBreaker do?
  // Then, why do I need to really call it and add load to it?
  // It will break the circuit (by stop calling the microservice Y and the X will directly return a
  // response back.

  // Now, we might be wondering, how do I know if the microservice is back up and I can call it
  // again?
  // And to be able to answer that, we'd need to understand how a CircuitBreaker works.
  // If We  go to the resilience4j documentation that we looked at earlier and click CircuitBreaker,
  // we'd see that there are multiple states for a CircuitBreaker.
  // A circuit breaker can be in three different states: CLOSED, OPEN and HALF_OPEN.
  //
  // CLOSED:
  // Is when we are calling the dependent microservice continuously.
  // So, in a closed state, we'll always be calling the dependent microservice
  //
  // OPEN:
  // In an open state,the CircuitBreaker will not call the dependent microservice,
  // it would directly return the fall-back response.
  //
  // HALF_OPEN:
  // in a half_open state, a CircuitBreaker would be sending a percentage of requests to the
  // dependent microservice and for rest of the requests,
  // it would return the hardcoded response or the fall-back response back.
  // The circuit will change state depending on your configuration.
  // It will change state according to this logic:
  // CLOSED -> OPEN
  // OPEN <-> HALF_OPEN
  // HALF_OPEN -> CLOSED

  // https://resilience4j.readme.io/docs/circuitbreaker
  // https://resilience4j.readme.io/docs/circuitbreaker#create-and-configure-a-circuitbreaker
  // https://resilience4j.readme.io/docs/getting-started-3
  @GetMapping("/sample-api-circuit-breaker")
  @CircuitBreaker(name = "default", fallbackMethod = "hardCodedResponse")
  public String sampleApiCircuitBreaker() {
    // let's make this fail and then, let's focus on retry.
    // So, what I would do is, I would actually create a new RestTemplate and let's just call some
    // dummy API.

    logger.info("Sample Api circuit breaker received call");
    ResponseEntity<String> forEntity =
        new RestTemplate().getForEntity("http://localhost:8000/some-dummy-url", String.class);
    return forEntity.getBody();
  }

  @GetMapping("/sample-api-rate-limiter")
  // What is rate limiting?
  // Basically, rate limiting is all about saying, in 10 seconds, we'd want to only allow 10000
  // calls API
  @RateLimiter(name = "default")
  // you can also configure how many concurrent calls are allowed.
  // That's called BulkHead.
  @Bulkhead(name = "default")
  public String sampleApiRateLimiter() {
    // We are sending 1 request per second.
    // watch -n 0.1 curl http://localhost:8000/sample-api-rate-limiter

    logger.info("Sample Api rate-limiter received call");

    return "Sample Api rate-limiter";
  }

  @GetMapping("/sample-api-rate-limiter-bulk-head")
  // Example of a custom configuration (instead of using the "default" config)
  @RateLimiter(name = "sample-api-rate-limiter")
  @Bulkhead(name = "sample-api-rate-limiter")
  public String sampleApiRateLimiterBulkheadCustomConfig() {
    // We are sending 1 request per second.
    // watch -n 0.1 curl http://localhost:8000/sample-api-rate-limiter-bulk-head

    logger.info("Sample Api rate-limiter-bulk-head received call");

    return "Sample Api rate-limiter-bulk-head";
  }

  // We can have different fallback methods for different kinds of exceptions.
  // Let's have a blanket method capturing everything, Exception ex
  // Exception ex in this code is meant to call all the exceptions that can be thrown
  private String hardCodedResponse(Exception ex) {
    logger.info("Fall back retry response");
    return "fallback-response";
  }
  // we looked at the retry features which are present in
  // resilience4j. These are useful when a service is momentarily down. You'd just give the service
  // a little of time and then call it.
}
