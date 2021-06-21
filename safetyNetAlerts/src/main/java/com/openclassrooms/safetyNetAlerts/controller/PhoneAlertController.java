package com.openclassrooms.safetyNetAlerts.controller;

import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.model.PhoneAlert;
import com.openclassrooms.safetyNetAlerts.service.PhoneAlertService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This url returns a list of telephone numbers of residents served by the firestation.
 * It will be used to send emergency text messages to specific households.
 */

@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlertController {

    private static final Logger logger = LogManager.getLogger(PhoneAlertController.class);

    @GetMapping
    public PhoneAlert getPhoneNumbers(@RequestParam("firestation") String firestation) {

        if (firestation.isEmpty()) {
            logger.error("Firestation is required");
            throw new BadRequestException("One or more parameters are wrong in request.");
            } else {
                return new PhoneAlertService.PhoneAlertBuilder().withStation(firestation).build();
            }
    }
}

