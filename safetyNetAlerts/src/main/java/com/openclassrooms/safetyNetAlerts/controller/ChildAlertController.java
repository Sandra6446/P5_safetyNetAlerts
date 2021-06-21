
package com.openclassrooms.safetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.openclassrooms.safetyNetAlerts.constants.FilterName;
import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.model.HouseHold;
import com.openclassrooms.safetyNetAlerts.service.CoverageByAddressService;
import com.openclassrooms.safetyNetAlerts.service.HouseHoldService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * This url returns a list of children (any individual aged 18 or under) living at this address.
 * The list includes each child's first and last name, age and a list of other household members.
 * If there are no children, this url returns an empty string.
 */

@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {

    private static final Logger logger = LogManager.getLogger(ChildAlertController.class);

    @GetMapping
    public MappingJacksonValue getChildInfos(@RequestParam("address") String address) {

        if (address.isEmpty()) {
            logger.error("Address is required");
            throw new BadRequestException("One or more parameters are wrong in request.");
        } else {
            FilterProvider filters = new SimpleFilterProvider()
                    .addFilter(FilterName.PERSON_INFO, SimpleBeanPropertyFilter.filterOutAllExcept("firstName", "lastName", "age"))
                    .addFilter(FilterName.HOUSEHOLD, SimpleBeanPropertyFilter.filterOutAllExcept("houseHoldMembers"));

            MappingJacksonValue response = new MappingJacksonValue(new HouseHoldService.HouseHoldBuilder().withAddress(address).onlyWithChild().build());

            response.setFilters(filters);

            return response;
        }
    }
}

