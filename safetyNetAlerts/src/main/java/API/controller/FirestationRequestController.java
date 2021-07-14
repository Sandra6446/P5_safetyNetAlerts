package API.controller;

import API.exceptions.BadRequestException;
import API.model.MyMap;
import API.service.CollectDataService;
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
 * Collects information about the coverage of a specific firestation
 */


@RestController
@RequestMapping("/firestation")
public class FirestationRequestController {

    private static final Logger logger = LogManager.getLogger(FirestationRequestController.class);

    @Autowired
    CollectDataService collectDataService;


    /**
     * Returns a list of people covered by a firestation
     *
     * @param station The station number of the firestation concerned by the alert
     * @return A list of people covered by the corresponding firestation. The list includes the following specific information: first name, last name, address, phone number. It also provides a count of the number of adults and the number of children (any individual aged 18 or under) in the area served.
     */
    @ApiOperation("Returns a list of people covered by a firestation")
    @GetMapping
    public MappingJacksonValue getDataPerFirestation(@RequestParam("stationNumber") String station) throws BadRequestException {

        if (station.isEmpty()) {
            logger.error("Station number is required");
            throw new BadRequestException("One or more parameters are wrong in request.");
        } else {

            List<MyMap> myMaps = collectDataService.buildMyMaps()
                    .stream()
                    .filter(mapOfList -> mapOfList.getStation().equals(station))
                    .collect(Collectors.toList());

            FilterProvider filterProvider = new SimpleFilterProvider().addFilter("myMapFilter", SimpleBeanPropertyFilter.serializeAllExcept("station"))
                    .addFilter("houseFilter", SimpleBeanPropertyFilter.serializeAllExcept("city", "zip"))
                    .addFilter("myPersonFilter", SimpleBeanPropertyFilter.serializeAllExcept("age", "email", "myMedicalRecord"));
            MappingJacksonValue response = new MappingJacksonValue(myMaps);
            response.setFilters(filterProvider);
            logger.info("Request correctly sent");
            return response;
        }
    }
}

