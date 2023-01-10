package com.mb.rest.webservices.apigateway.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter {

  // Spring Cloud Gateway is an awesome way to route to your APIs and implement your cross-cutting
  // concerns
  // things like:
  // security, monitoring/metrics.
  // These are the best things that we can implement in a Spring Cloud Gateway.
  // Some important features of Spring Cloud Gateway are it can match requests on any request
  // attribute.
  // Spring Cloud Gateway integrates with the Spring Cloud Discovery Client as well, it also
  // provides load balancing.
  // we can do a path rewriting using Spring Cloud Gateway.
  private final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    logger.info("Path of the received request -> {}", exchange.getRequest().getPath());
    return chain.filter(exchange);
  }
}
