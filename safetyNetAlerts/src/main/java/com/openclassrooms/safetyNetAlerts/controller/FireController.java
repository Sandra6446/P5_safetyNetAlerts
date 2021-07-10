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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Collects specific information useful in case of fire
 */

@RestController
@RequestMapping("/fire")
public class FireController {

    private static final Logger logger = LogManager.getLogger(FireController.class);

    @Autowired
    private CollectDataService collectDataService;

    /**
     * Search all the inhabitants living at the given address and the number of the firestation serving it
     *
     * @param address The address concerned by the alert
     * @return The list of inhabitants living at the given address and the station number of the firestation serving it. The list includes the name, phone number, age and medical history (medication, dosage and allergies) of each person.
     */
    @GetMapping
    public MappingJacksonValue getDataPerAddress(@RequestParam("address") String address) throws BadRequestException {

        if (address.isEmpty()) {
            logger.error("The address is required");
            throw new BadRequestException("One or more parameters are wrong in request.");
        } else {
            List<MyMap> myMaps = collectDataService.buildMyMaps()
                    .stream()
                    .filter(myMap -> myMap.getHouses().stream().anyMatch(house -> house.getStreet().equals(address)))
                    .peek(myMap -> myMap.filterByAddress(address))
                    .collect(Collectors.toList());

            FilterProvider filterProvider = new SimpleFilterProvider().addFilter("myMapFilter", SimpleBeanPropertyFilter.serializeAllExcept("nbOfAdults", "nbOfChildren"))
                    .addFilter("houseFilter", SimpleBeanPropertyFilter.filterOutAllExcept("residents"))
                    .addFilter("myPersonFilter", SimpleBeanPropertyFilter.serializeAllExcept("firstName", "email"));
            MappingJacksonValue response = new MappingJacksonValue(myMaps);
            response.setFilters(filterProvider);
            logger.info("Request correctly sent");
            return response;
        }
    }
}
