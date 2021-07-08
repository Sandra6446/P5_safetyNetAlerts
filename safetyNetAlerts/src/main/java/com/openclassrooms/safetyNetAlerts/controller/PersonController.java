package com.openclassrooms.safetyNetAlerts.controller;

import com.openclassrooms.safetyNetAlerts.dao.PersonsDAO;
import com.openclassrooms.safetyNetAlerts.exceptions.AlreadyInDataFileException;
import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.exceptions.NotFoundInDataFileException;
import com.openclassrooms.safetyNetAlerts.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.net.URI;
import java.util.Set;

/**
 * Updates the list of persons in the json data file
 */

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger = LogManager.getLogger(PersonController.class);
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();
    @Autowired
    private PersonsDAO personsDAO;

    /**
     * Adds a person in json data file
     *
     * @param person The person to be added to the data file
     * @return The status of the request
     */
    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Person person) throws BadRequestException {

        Set<ConstraintViolation<Person>> constraintViolations =
                validator.validate(person);

        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<Person> constraint : constraintViolations) {
                logger.error(constraint.getRootBeanClass().getSimpleName() +
                        "." + constraint.getPropertyPath() + " " + constraint.getMessage());
            }
            throw new BadRequestException("One or more parameters are wrong in body request.");
        } else {
            try {
                personsDAO.save(person);
                logger.info(person.getFirstName() + " " + person.getLastName() + " correctly added.");
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequestUri()
                        .buildAndExpand()
                        .toUri();
                return ResponseEntity.created(location).build();
            } catch (AlreadyInDataFileException s) {
                logger.error(person.getFirstName() + " " + person.getLastName() + " already exists in data file.");
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
    }

    /**
     * Updates a person in data file
     *
     * @param person The person to be updated in the data file
     * @return The status of the request
     */
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Person person) throws BadRequestException {

        Set<ConstraintViolation<Person>> constraintViolations =
                validator.validate(person);

        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<Person> constraint : constraintViolations) {
                logger.error(constraint.getRootBeanClass().getSimpleName() +
                        "." + constraint.getPropertyPath() + " " + constraint.getMessage());
            }
            throw new BadRequestException("One or more parameters are wrong in body request.");
        } else {
            try {
                personsDAO.update(person);
                logger.info(person.getFirstName() + " " + person.getLastName() + " correctly updated.");
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (NotFoundInDataFileException ne) {
                logger.error(person.getFirstName() + " " + person.getLastName() + " doesn't exist in data file.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    /**
     * Deletes a person in data file
     *
     * @param person The person to be deleted in the data file
     * @return The status of the request
     */
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Person person) throws BadRequestException {
        if (person.getFirstName().isEmpty() || person.getLastName().isEmpty()) {
            throw new BadRequestException("Firstname or Lastname are missing.");
        } else {
            try {
                personsDAO.remove(person);
                logger.info(person.getFirstName() + " " + person.getLastName() + " correctly deleted.");
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (NotFoundInDataFileException ne) {
                logger.error(person.getFirstName() + " " + person.getLastName() + " doesn't exist in data file.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }
}
