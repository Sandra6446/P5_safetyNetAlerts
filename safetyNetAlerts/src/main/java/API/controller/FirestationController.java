package API.controller;

import API.dao.FirestationsDAO;
import API.exceptions.AlreadyInDataFileException;
import API.exceptions.BadRequestException;
import API.exceptions.NotFoundInDataFileException;
import API.model.Firestation;
import io.swagger.annotations.ApiOperation;
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
 * Updates the list of firestations in the json data file
 */

@RestController
@RequestMapping("/firestation")
public class FirestationController {

    private static final Logger logger = LogManager.getLogger(FirestationController.class);
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();
    @Autowired
    private FirestationsDAO firestationsDAO;

    /**
     * Adds a firestation in json data file
     *
     * @param firestation The firestation to be added to the data file
     * @return The http status of the request
     */
    @ApiOperation(value = "Adds a firestation in json data file")
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
                firestationsDAO.save(firestation);
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

    /**
     * Updates a firestation in the data file
     *
     * @param firestation The firestation to be updated in the data file
     * @return The http status of the request
     */
    @ApiOperation("Updates a firestation in the data file")
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
                firestationsDAO.update(firestation);
                logger.info("Firestation number " + firestation.getStation() + " at " + firestation.getAddress() + " correctly updated.");
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (NotFoundInDataFileException ne) {
                logger.error("There is no firestation at " + firestation.getAddress() + " in data file.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    /**
     * Deletes a firestation in the data file
     *
     * @param firestation The firestation to be deleted in the data file
     * @return The http status of the request
     */
    @ApiOperation("Deletes a firestation in the data file")
    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestBody Firestation firestation) throws BadRequestException {
        if (firestation.getAddress().isEmpty()) {
            throw new BadRequestException("Firstname or Lastname are missing.");
        } else {
            try {
                firestationsDAO.remove(firestation);
                logger.info("Firestation number " + firestation.getStation() + " at " + firestation.getAddress() + " correctly deleted.");
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (NotFoundInDataFileException ne) {
                logger.error("There is no firestation at " + firestation.getAddress() + " in data file.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }
}
