package com.safetynetalert.microservicemedicalRecord.controller;

import dao.MedicalRecordDAO;
import exceptions.AlreadyInDataFileException;
import exceptions.BadRequestException;
import exceptions.NotFoundInDataFileException;
import model.MedicalRecord;
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
@Import(value = MedicalRecordDAO.class)
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordDAO medicalRecordDAO;

    private static final Logger logger = LogManager.getLogger(MedicalRecordController.class);

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody MedicalRecord medicalRecord) throws BadRequestException {

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
                medicalRecordDAO.save(medicalRecord);
                logger.info("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " correctly added.");
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequestUri()
                        .buildAndExpand()
                        .toUri();
                return ResponseEntity.created(location).build();
            } catch (AlreadyInDataFileException s) {
                logger.error("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " already exists in data file.");
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody MedicalRecord medicalRecord) throws BadRequestException {

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
                medicalRecordDAO.update(medicalRecord);
                logger.info("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " correctly updated.");
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (NotFoundInDataFileException ne) {
                logger.error("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " doesn't exist in data file.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody MedicalRecord medicalRecord) throws BadRequestException {
        if (medicalRecord.getFirstName().isEmpty() || medicalRecord.getLastName().isEmpty()) {
            throw new BadRequestException("Firstname or Lastname are missing.");
        } else {
            try {
                medicalRecordDAO.remove(medicalRecord);
                logger.info("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " correctly deleted.");
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (NotFoundInDataFileException ne){
                logger.error("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " doesn't exist in data file.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }
}
