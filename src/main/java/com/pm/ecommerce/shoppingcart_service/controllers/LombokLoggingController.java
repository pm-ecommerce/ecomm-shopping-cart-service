package com.pm.ecommerce.shoppingcart_service.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Log4j2
public class LombokLoggingController {

    @RequestMapping("/lombok")
    public String index(){
        log.trace("log TRACE");
        log.debug("log DEBUG");
        log.info("log INFO");
        log.warn("log warn");
        log.error("log error");
        return "CHECK OUT THE LOG OUTPUT";
    }
}
