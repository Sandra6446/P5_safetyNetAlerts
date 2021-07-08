package com.openclassrooms.safetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.model.Family;
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
 * Collects specific information about children
 */

@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {

    private static final Logger logger = LogManager.getLogger(ChildAlertController.class);

    @Autowired
    private CollectDataService collectDataService;

    /**
     * Search all the children (any individual aged 18 or under) living at an address.
     *
     * @param address The address concerned by the alert
     * @return A list of children (any individual aged 18 or under) living at this address. The list includes each child's first and last name, age and a list of other household members. If there are no children, this url returns an empty string.
     */
    @GetMapping
    public MappingJacksonValue getChildInfos(@RequestParam("address") String address) throws BadRequestException {

        if (address.isEmpty()) {
            logger.error("Address is required");
            throw new BadRequestException("One or more parameters are wrong in request.");
        } else {
            List<Family> families = collectDataService.buildFamilies().stream().filter(family -> family.getStreet().equals(address) && !family.getChildren().isEmpty()).collect(Collectors.toList());
            FilterProvider filterProvider = new SimpleFilterProvider().addFilter("familyFilter", SimpleBeanPropertyFilter.filterOutAllExcept("children", "adults"))
                    .addFilter("childrenFilter", SimpleBeanPropertyFilter.filterOutAllExcept("firstName", "lastName", "age"))
                    .addFilter("adultsFilter", SimpleBeanPropertyFilter.filterOutAllExcept("firstName", "lastName"));
            MappingJacksonValue response = new MappingJacksonValue(families);
            response.setFilters(filterProvider);
            logger.info("Request correctly sent");
            return response;
        }
    }
}
