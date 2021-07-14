package API.controller;

import API.exceptions.BadRequestException;
import API.model.Contact;
import API.model.MyMap;
import API.service.CollectDataService;
import API.util.UtilsHouse;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.ApiOperation;
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
 * Collects phone numbers to send emergency text messages to specific households
 */

@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlertController {

    private static final Logger logger = LogManager.getLogger(PhoneAlertController.class);

    @Autowired
    CollectDataService collectDataService;

    /**
     * Returns the phone numbers of residents served by a firestation
     *
     * @param firestation The station number of the firestation concerned by the alert
     * @return The list of phone numbers of residents served by the firestation
     */
    @ApiOperation("Returns the phone numbers of residents served by a firestation")
    @GetMapping
    public MappingJacksonValue getPhoneNumbers(@RequestParam("firestation") String firestation) throws BadRequestException {

        if (firestation.isEmpty()) {
            logger.error("Firestation is required");
            throw new BadRequestException("One or more parameters are wrong in request.");
        } else {
            List<MyMap> myMaps = collectDataService.buildMyMaps().stream().filter(map -> map.getStation().equals(firestation)).collect(Collectors.toList());
            Contact contact = new Contact();
            for (MyMap myMap : myMaps) {
                UtilsHouse.putDetailsInContact(myMap.getHouses(), contact);
            }
            FilterProvider filters = new SimpleFilterProvider().addFilter("contactFilter", SimpleBeanPropertyFilter.filterOutAllExcept("phoneNumbers"));
            MappingJacksonValue response = new MappingJacksonValue(contact);
            response.setFilters(filters);
            logger.info("Request correctly sent");
            return response;
        }
    }
}

