package com.openclassrooms.safetyNetAlerts.controller;

import com.openclassrooms.safetyNetAlerts.dao.FirestationDAO;
import com.openclassrooms.safetyNetAlerts.exceptions.AlreadyInDataFileException;
import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.exceptions.NotFoundInDataFileException;
import com.openclassrooms.safetyNetAlerts.model.Firestation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
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

@RestController
@Import(value = FirestationDAO.class)
@RequestMapping("/firestation")
public class FirestationController {

    @Autowired
    private FirestationDAO firestationDAO;

    private static final Logger logger = LogManager.getLogger(Firestation.class);

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Firestation firestation) throws BadRequestException {

        Set<ConstraintViolation<Firestation>> constraintViolations =
                validator.validate(firestation);

        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<Firestation> constraint : constraintViolations) {
                logger.error(constraint.getRootBeanClass().getSimpleName() +
                        "." + constraint.getPropertyPath() + " " + constraint.getMessage());
            }
            throw new BadRequestException("One or more parameters are wrong in body request.");
        } else {
            try {
                firestationDAO.save(firestation);
                logger.info("Firestation number " + firestation.getStation() + " at " + firestation.getAddress() + " correctly added.");
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequestUri()
                        .buildAndExpand()
                        .toUri();
                return ResponseEntity.created(location).build();
            } catch (AlreadyInDataFileException s) {
                logger.error("Firestation number " + firestation.getStation() + " at " + firestation.getAddress() + " already exists in data file.");
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Firestation firestation) throws BadRequestException {

        Set<ConstraintViolation<Firestation>> constraintViolations =
                validator.validate(firestation);

        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<Firestation> constraint : constraintViolations) {
                logger.error(constraint.getRootBeanClass().getSimpleName() +
                        "." + constraint.getPropertyPath() + " " + constraint.getMessage());
            }
            throw new BadRequestException("One or more parameters are wrong in body request.");
        } else {
            try {
                firestationDAO.update(firestation);
                logger.info("Firestation number " + firestation.getStation() + " at " + firestation.getAddress() + " correctly updated.");
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (NotFoundInDataFileException ne) {
                logger.error("There is no firestation at " + firestation.getAddress() + " in data file.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Firestation firestation) throws BadRequestException {
        if (firestation.getAddress().isEmpty()) {
            throw new BadRequestException("Firstname or Lastname are missing.");
        } else {
            try {
                firestationDAO.remove(firestation);
                logger.info("Firestation number " + firestation.getStation() + " at " + firestation.getAddress() + " correctly deleted.");
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (NotFoundInDataFileException ne){
                logger.error("There is no firestation at " + firestation.getAddress() + " in data file.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }
}
