package API.controller;

import API.exceptions.BadRequestException;
import API.model.Contact;
import API.model.House;
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
 * Collects emails to send emergency messages to specific households
 */

@RestController
@RequestMapping("/communityEmail")
public class CommunityEmailController {

    private static final Logger logger = LogManager.getLogger(CommunityEmailController.class);

    @Autowired
    CollectDataService collectDataService;

    /**
     * Returns all the phone numbers of residents served by the firestation
     *
     * @param city The city concerned by the alert
     * @return A list of the phone numbers of residents served by the firestation
     */
    @ApiOperation(value = "Returns all the phone numbers of residents served by the firestation")
    @GetMapping
    public MappingJacksonValue getEmails(@RequestParam("city") String city) throws BadRequestException {

        if (city.isEmpty()) {
            logger.error("City is required");
            throw new BadRequestException("One or more parameters are wrong in request.");
        } else {
            List<House> houses = collectDataService.buildHouses().stream().filter(house -> house.getCity().equals(city)).collect(Collectors.toList());
            Contact contact = new Contact();
            UtilsHouse.putDetailsInContact(houses, contact);

            FilterProvider filters = new SimpleFilterProvider().addFilter("contactFilter", SimpleBeanPropertyFilter.filterOutAllExcept("emails"));
            MappingJacksonValue response = new MappingJacksonValue(contact);
            response.setFilters(filters);
            logger.info("Request correctly sent");
            return response;
        }
    }
}


