package com.mb.rest.webservices.apigateway.configuration;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfiguration {

  public static final String LB_CURRENCY_CONVERSION = "lb://currency-conversion";

  @Bean
  public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {

    // add custom filters on specific paths. In API gateway

    // what we are doing in here is creating a simple route function.
    // If a request comes to /get, then we would want to redirect it to this specific
    // URI "https://http.org:80".

    // We can addRequestHeader and/or addRequestParameter values before routing the request to the new URL,
    // let's say, we would want to add in a few authentication parameters.
    Function<PredicateSpec, Buildable<Route>> routeFunction =
            p ->
            p.path("/get")
                .filters(f -> f.addRequestHeader("MyHeader", "MyURI")
                               .addRequestParameter("Param","MyParam"))
                .uri("http://httpbin.org:80");

    // If a request URI starts with currency-exchange, what we would want to do is to
    // redirect it using the naming server, and we would also want to do load balancing.
    // The way you can do that is by just putting in
    // lb:// and the name of the registration on the Eureka server.
    // So, on the Eureka server (currency-exchange), this is registered as currency-exchange.
    Function<PredicateSpec, Buildable<Route>> currencyExchangeRouteFunction =
        p -> p.path("/currency-exchange/**")
                .uri("lb://currency-exchange");

    Function<PredicateSpec, Buildable<Route>> currencyConversionRestTemplateFunction =
            p -> p.path("/currency-conversion/**")
                    .uri(LB_CURRENCY_CONVERSION);

    Function<PredicateSpec, Buildable<Route>> currencyConversionFeignFunction =
            p -> p.path("/currency-conversion-feign/**")
                    .uri(LB_CURRENCY_CONVERSION);

    // currency-conversion-new/(the next thing) and /currency-conversion-feign/(the next thing)
    // we would want to define the regular expression, identifying the next thing as a segment.
    // So, whatever follows currency-conversion-new, we would want to append it to currency-conversion-feign.
    // The way I can do that in here is by saying ${segment}.
    Function<PredicateSpec, Buildable<Route>> currencyConversionNewFeignFunction =
        p ->
            p.path("/currency-conversion-new/**")
                .filters(
                    f ->
                        f.rewritePath(
                            "/currency-conversion-new/(?<segment>.*)",
                            "/currency-conversion-feign/${segment}"))
                .uri(LB_CURRENCY_CONVERSION);

    return builder.routes()
            .route(routeFunction)
            .route(currencyExchangeRouteFunction)
            .route(currencyConversionRestTemplateFunction)
            .route(currencyConversionFeignFunction)
            .route(currencyConversionNewFeignFunction)
            .build();
  }

  // We learned how to build routes through the Eureka Naming Server and redirect all the requests
  // through the API Gateway. In this step, let's look at how you can build custom routes.
  // One of the options to build custom routes is to actually create a configuration file.

  // the main idea is to redirect
  // http://localhost:8765/currency-exchange/currency-exchange/from/EUR/to/CAD (the proxy link) and redirect
  // to http://localhost:8765/currency-exchange/from/EUR/to/CAD (naming server)
}
