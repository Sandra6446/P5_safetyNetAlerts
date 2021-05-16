package com.safetynetalert.microserviceperson.controller;

import com.safetynetalert.microserviceperson.dao.PersonDAO;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.*;
import java.net.URI;
import java.util.Set;

@RequestMapping("/person")
@RestController
public class PersonController {

    @Autowired
    private PersonDAO personDAO;

    private static final Logger logger = LogManager.getLogger(PersonController.class);

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Person person) {

        Set<ConstraintViolation<Person>> constraintViolations =
                validator.validate(person);

        if (constraintViolations.size() > 0) {
            logger.error("One or more parameters are wrong in body request.");
            for (ConstraintViolation<Person> constraint : constraintViolations) {
                logger.error(constraint.getRootBeanClass().getSimpleName() +
                        "." + constraint.getPropertyPath() + " " + constraint.getMessage());
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (personDAO.save(person) != null) {
                logger.info(person.getFirstName() + " " + person.getLastName() + " correctly added.");
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequestUri()
                        .buildAndExpand()
                        .toUri();
                return ResponseEntity.created(location).build();
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Person person) {

        Set<ConstraintViolation<Person>> constraintViolations =
                validator.validate(person);

        if (constraintViolations.size() > 0) {
            logger.error("One or more parameters are wrong in body request.");
            for (ConstraintViolation<Person> constraint : constraintViolations) {
                logger.error(constraint.getRootBeanClass().getSimpleName() +
                        "." + constraint.getPropertyPath() + " " + constraint.getMessage());
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (personDAO.update(person) != null) {
                logger.info(person.getFirstName() + " " + person.getFirstName() + " correctly updated.");
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Person person) {
        if (person.getFirstName().isEmpty() || person.getLastName().isEmpty()) {
            logger.error("Firstname or Lastname are missing");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (personDAO.remove(person)) {
                logger.info(person.getFirstName() + " " + person.getFirstName() + " correctly deleted.");
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }
}
