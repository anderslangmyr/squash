package com.langmyr.squash.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.langmyr.squash.log.AuditLogFilter;

@RestController
public class PingController {
	private static Logger logger = LoggerFactory.getLogger(PingController.class);

    @RequestMapping(value="/api/ping", produces="text/plain")
    public String ping() {
    	logger.info("JAU");
        return "pong";
    }
}
