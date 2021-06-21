
package com.openclassrooms.safetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.openclassrooms.safetyNetAlerts.constants.FilterName;
import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.service.CoverageByAddressService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * This url returns the list of inhabitants living at the given address and the number of the fire station serving it.
 * The list includes the name, phone number, age and medical history (medication, dosage and allergies) of each person.
 */


@RestController
@RequestMapping("/fire")
public class FireController {

    private static final Logger logger = LogManager.getLogger(FireController.class);

    @GetMapping
    public MappingJacksonValue getDataPerAddress(@RequestParam("address") String address) {

        if (address.isEmpty()) {
            logger.error("The address is required");
            throw new BadRequestException("One or more parameters are wrong in request.");
        } else {
            FilterProvider filters = new SimpleFilterProvider()
                    .addFilter(FilterName.COVERAGE_BY_ADDRESS,SimpleBeanPropertyFilter.filterOutAllExcept("houseHoldMembers","firestations"))
                    .addFilter(FilterName.PERSON_INFO,SimpleBeanPropertyFilter.filterOutAllExcept("firstName","lastName","phone","age","medicalRecord"))
                    .addFilter(FilterName.FIRESTATIONS,SimpleBeanPropertyFilter.filterOutAllExcept("station"))
                    .addFilter(FilterName.MEDICAL_RECORD,SimpleBeanPropertyFilter.filterOutAllExcept("medications","allergies"));

            MappingJacksonValue response = new MappingJacksonValue(new CoverageByAddressService.CoverageByAddressBuilder().withAddress(address).build());

            response.setFilters(filters);

            return response;
        }


    }
}

