package com.udemy.microservices.currencyconversionservice.controller;

import com.udemy.microservices.currencyconversionservice.model.CurrencyConversion;
import com.udemy.microservices.currencyconversionservice.service.CurrencyExchangeProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy proxy;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateConversion(@PathVariable String from,
                                                  @PathVariable String to,
                                                  @PathVariable BigDecimal quantity) {

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversion> response = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                CurrencyConversion.class, uriVariables);

        CurrencyConversion currencyConversion = response.getBody();

        return CurrencyConversion.builder()
                .id(currencyConversion.getId())
                .from(currencyConversion.getFrom())
                .to(currencyConversion.getTo())
                .quantity(quantity)
                .conversionMultiple(currencyConversion.getConversionMultiple())
                .totalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiple()))
                .environment(currencyConversion.getEnvironment()).build();

    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateConversionFeign(@PathVariable String from,
                                                       @PathVariable String to,
                                                       @PathVariable BigDecimal quantity) {
        log.info("calculateConversionFeign method params -> from: {}, to: {}, quantity: {}", from, to, quantity);
        CurrencyConversion currencyConversion = proxy.getExchangeValue(from, to);
        log.info("currencyConversion object -> {}", currencyConversion);

        return CurrencyConversion.builder()
                .id(currencyConversion.getId())
                .from(currencyConversion.getFrom())
                .to(currencyConversion.getTo())
                .quantity(quantity)
                .conversionMultiple(currencyConversion.getConversionMultiple())
                .totalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiple()))
                .environment(currencyConversion.getEnvironment()).build();

    }
}
