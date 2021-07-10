package com.openclassrooms.safetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.model.MyMap;
import com.openclassrooms.safetyNetAlerts.service.CollectDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Collects specific information useful in case of fire
 */

@RestController
@RequestMapping("/flood/stations")
public class FloodController {

    private static final Logger logger = LogManager.getLogger(FloodController.class);

    @Autowired
    CollectDataService collectDataService;

    /**
     * Search all the households served by a firestation
     *
     * @param stations The list of the station numbers of firestations concerned by the alert
     * @return The list of all households served by the firestation. This list groups people by address. It also includes the name, phone number and age of the residents, and their medical history (medication, dosage and allergies).
     */
    @GetMapping
    public MappingJacksonValue getHouseHolds(@RequestParam("stations") Set<String> stations) throws BadRequestException {

        if (stations.isEmpty()) {
            logger.error("At less one station is required");
            throw new BadRequestException("One or more parameters are wrong in request.");
        } else {
            List<MyMap> myMaps = new ArrayList<>();
            for (String station : stations) {
                myMaps.addAll(collectDataService.buildMyMaps().stream().filter(map -> map.getStation().equals(station)).collect(Collectors.toList()));
            }

            FilterProvider filterProvider = new SimpleFilterProvider().addFilter("myMapFilter", SimpleBeanPropertyFilter.filterOutAllExcept("houses"))
                    .addFilter("houseFilter", SimpleBeanPropertyFilter.serializeAllExcept("city", "zip"))
                    .addFilter("myPersonFilter", SimpleBeanPropertyFilter.serializeAllExcept("firstName", "email"));
            MappingJacksonValue response = new MappingJacksonValue(myMaps);
            response.setFilters(filterProvider);
            logger.info("Request correctly sent");
            return response;
        }
    }
}

