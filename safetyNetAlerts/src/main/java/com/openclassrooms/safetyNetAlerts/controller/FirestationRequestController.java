
package com.openclassrooms.safetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.openclassrooms.safetyNetAlerts.constants.FilterName;
import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.service.CoverageByStationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * This url returns a list of people covered by the corresponding fire station.
 * So, if the station number = 1, it returns the inhabitants covered by station number 1.
 * The list includes the following specific information: first name, last name, address, phone number.
 * It also provides a count of the number of adults and the number of children (any individual aged 18 or under) in the area served.
 */


@RestController
@RequestMapping("/firestation")
public class FirestationRequestController {

    private static final Logger logger = LogManager.getLogger(FirestationRequestController.class);

    @GetMapping
    public MappingJacksonValue getDataPerFirestation(@RequestParam("stationNumber") String station) {

        if (station.isEmpty()) {
            logger.error("Station number is required");
            throw new BadRequestException("One or more parameters are wrong in request.");
        } else {
            FilterProvider filters = new SimpleFilterProvider()
                    .addFilter(FilterName.PERSON_INFO, SimpleBeanPropertyFilter.filterOutAllExcept("firstName", "lastName", "address", "phone"));

            MappingJacksonValue response = new MappingJacksonValue(new CoverageByStationService.CoverageByStationBuilder().withStation(station).build());

            response.setFilters(filters);

            return response;
        }


    }
}

