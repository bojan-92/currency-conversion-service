package com.udemy.microservices.currencyconversionservice.service;

import com.udemy.microservices.currencyconversionservice.model.CurrencyConversion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "currency-exchange", url = "localhost:8000")

// talk to Eureka and get instance of currency-exchange by name and do load balancing between them
@FeignClient(name = "currency-exchange")
public interface CurrencyExchangeProxy {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    CurrencyConversion getExchangeValue(@PathVariable String from, @PathVariable String to);
}
