package API.controller;

import API.exceptions.BadRequestException;
import API.model.MyPerson;
import API.service.CollectDataService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import API.model.House;
import io.swagger.annotations.ApiOperation;
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
import java.util.stream.Collectors;

/**
 * Collects information about specific inhabitants
 */

@RestController
@RequestMapping("/personInfo")
public class PersonInfoController {

    private static final Logger logger = LogManager.getLogger(PersonInfoController.class);

    @Autowired
    CollectDataService collectDataService;

    /**
     * Returns the address, age, email address and medical history (medication, dosage, allergies) of specific inhabitants
     *
     * @param firstName The firstname of people concerned by the alert
     * @param lastName  The lastname of people concerned by the alert
     * @return returns the name, address, age, email address and medical history (medication, dosage, allergies) of each inhabitant with this firstname and lastname.
     */
    @ApiOperation("Returns the address, age, email address and medical history (medication, dosage, allergies) of specific inhabitants")
    @GetMapping
    public MappingJacksonValue getPersonInfos(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) throws BadRequestException {

        if (firstName.isEmpty() || lastName.isEmpty()) {
            logger.error("The firstname and the lastName are required");
            throw new BadRequestException("One or more parameters are wrong in request.");
        } else {
            List<House> houses = new ArrayList<>();
            for (House house : collectDataService.buildHouses()) {
                List<MyPerson> residents = house.getResidents().stream().filter(myPerson -> myPerson.getFirstName().equals(firstName) && myPerson.getLastName().equals(lastName)).collect(Collectors.toList());
                if (!residents.isEmpty()) {
                    house.setResidents(residents);
                    houses.add(house);
                }
            }

            FilterProvider filterProvider = new SimpleFilterProvider().addFilter("houseFilter", SimpleBeanPropertyFilter.serializeAllExcept("city", "zip"))
                    .addFilter("myPersonFilter", SimpleBeanPropertyFilter.serializeAllExcept("firstName", "phone"));
            MappingJacksonValue response = new MappingJacksonValue(houses);
            response.setFilters(filterProvider);
            logger.info("Request correctly sent");
            return response;
        }
    }
}

