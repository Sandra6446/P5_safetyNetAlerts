package com.openclassrooms.safetyNetAlerts.controller;

import dao.PersonDAO;
import exceptions.BadRequestException;
import exceptions.AlreadyInDataFileException;
import exceptions.NotFoundInDataFileException;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.*;
import java.net.URI;
import java.util.Set;

@RestController
@Import(value = PersonDAO.class)
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonDAO personDAO;

    private static final Logger logger = LogManager.getLogger(PersonController.class);

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

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
                personDAO.save(person);
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
                personDAO.update(person);
                logger.info(person.getFirstName() + " " + person.getLastName() + " correctly updated.");
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (NotFoundInDataFileException ne) {
                logger.error(person.getFirstName() + " " + person.getLastName() + " doesn't exist in data file.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Person person) throws BadRequestException {
        if (person.getFirstName().isEmpty() || person.getLastName().isEmpty()) {
            throw new BadRequestException("Firstname or Lastname are missing.");
        } else {
            try {
                personDAO.remove(person);
                logger.info(person.getFirstName() + " " + person.getLastName() + " correctly deleted.");
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (NotFoundInDataFileException ne){
                logger.error(person.getFirstName() + " " + person.getLastName() + " doesn't exist in data file.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }
}
