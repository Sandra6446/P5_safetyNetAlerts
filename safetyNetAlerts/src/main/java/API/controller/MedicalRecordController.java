package API.controller;

import API.dao.MedicalRecordsDAO;
import API.exceptions.BadRequestException;
import API.exceptions.AlreadyInDataFileException;
import API.exceptions.NotFoundInDataFileException;
import API.model.MedicalRecord;
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
 * Updates the list of medicalRecords in the json data file
 */

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private static final Logger logger = LogManager.getLogger(MedicalRecordController.class);
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();
    @Autowired
    private MedicalRecordsDAO medicalRecordsDAO;

    /**
     * Adds a medicalRecord in json data file
     *
     * @param medicalRecord The medicalRecord to be added to the data file
     * @return The status of the request
     */
    @ApiOperation("Adds a medicalRecord in json data file")
    @PostMapping
    public ResponseEntity<String> add(@RequestBody MedicalRecord medicalRecord) throws BadRequestException {

        Set<ConstraintViolation<MedicalRecord>> constraintViolations =
                validator.validate(medicalRecord);

        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<MedicalRecord> constraint : constraintViolations) {
                logger.error(constraint.getRootBeanClass().getSimpleName() +
                        "." + constraint.getPropertyPath() + " " + constraint.getMessage());
            }
            throw new BadRequestException("One or more parameters are wrong in body request.");
        } else {
            try {
                medicalRecordsDAO.save(medicalRecord);
                logger.info("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " correctly added.");
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequestUri()
                        .buildAndExpand()
                        .toUri();
                return ResponseEntity.created(location).body("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " correctly added.");
            } catch (AlreadyInDataFileException s) {
                logger.info("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " already exists in data file.");
                return new ResponseEntity<>("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " already exists in data file.",HttpStatus.CONFLICT);
            }
        }
    }

    /**
     * Updates a medicalRecord in data file
     *
     * @param medicalRecord The medicalRecord to be updated in the data file
     * @return The status of the request
     */
    @ApiOperation("Updates a medicalRecord in data file")
    @PutMapping
    public ResponseEntity<String> update(@RequestBody MedicalRecord medicalRecord) throws BadRequestException {

        Set<ConstraintViolation<MedicalRecord>> constraintViolations =
                validator.validate(medicalRecord);

        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<MedicalRecord> constraint : constraintViolations) {
                logger.error(constraint.getRootBeanClass().getSimpleName() +
                        "." + constraint.getPropertyPath() + " " + constraint.getMessage());
            }
            throw new BadRequestException("One or more parameters are wrong in body request.");
        } else {
            try {
                medicalRecordsDAO.update(medicalRecord);
                logger.info("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " correctly updated.");
                return ResponseEntity.ok("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " correctly updated.");
            } catch (NotFoundInDataFileException ne) {
                logger.info("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " doesn't exist in data file.");
                return new ResponseEntity<>("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " doesn't exist in data file.",HttpStatus.NOT_FOUND);
            }
        }
    }

    /**
     * Deletes a medicalRecord in data file
     *
     * @param medicalRecord The medicalRecord to be deleted in the data file
     * @return The status of the request
     */
    @ApiOperation("Deletes a medicalRecord in data file")
    @DeleteMapping
    public ResponseEntity<String> delete(@RequestBody MedicalRecord medicalRecord) throws BadRequestException {
        Set<ConstraintViolation<MedicalRecord>> constraintViolations =
                validator.validate(medicalRecord);

        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<MedicalRecord> constraint : constraintViolations) {
                logger.error(constraint.getRootBeanClass().getSimpleName() +
                        "." + constraint.getPropertyPath() + " " + constraint.getMessage());
            }
            throw new BadRequestException("One or more parameters are wrong in body request.");
        } else {
            try {
                medicalRecordsDAO.remove(medicalRecord);
                logger.info("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " correctly deleted.");
                return ResponseEntity.ok("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " correctly deleted.");
            } catch (NotFoundInDataFileException ne) {
                logger.info("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " doesn't exist in data file.");
                return new ResponseEntity<>("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " doesn't exist in data file.",HttpStatus.NOT_FOUND);
            }
        }
    }
}
