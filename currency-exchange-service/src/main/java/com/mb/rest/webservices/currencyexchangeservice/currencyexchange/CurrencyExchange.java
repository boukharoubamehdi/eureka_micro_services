package com.mb.rest.webservices.currencyexchangeservice.currencyexchange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CurrencyExchange {

  @Id @GeneratedValue private Long id;

  @Column(name = "currency_from")
  private String from;

  @Column(name = "currency_to")
  private String to;

  private BigDecimal conversionMultiple;
  // To be able to track the instance of Currency Exchange that is providing the response back,
  // the port where the response is coming back from.

  @Column(name = "port")
  private String environment;
}
