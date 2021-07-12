package com.openclassrooms.safetyNetAlerts.dao;

import com.openclassrooms.safetyNetAlerts.exceptions.AlreadyInDataFileException;
import com.openclassrooms.safetyNetAlerts.exceptions.NotFoundInDataFileException;
import com.openclassrooms.safetyNetAlerts.model.JsonObject;
import com.openclassrooms.safetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.safetyNetAlerts.util.JsonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Reads and updates the list of medicalRecords in json data file
 */

@Component
public class MedicalRecordsDAO implements IDataInJsonDao<MedicalRecord> {

    private static final Logger logger = LogManager.getLogger(MedicalRecordsDAO.class);

    @Autowired
    private JsonMapper jsonMapper;

    /**
     * Reads the list of medicalRecords in json data file
     *
     * @return A list of all the medicalRecords from data file
     */
    @Override
    public List<MedicalRecord> getAll() {
        JsonObject jsonObject = jsonMapper.readJson();
        List<MedicalRecord> medicalRecords = jsonObject.getMedicalrecords();

        return medicalRecords;
    }

    /**
     * Adds a medicalRecord in json data file
     *
     * @param medicalRecord The medical record to be added.
     * @return If the medical record to be added is already in data base, then "null" is returned, otherwise, the medical record added is returned.
     */
    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) throws AlreadyInDataFileException {

        JsonObject jsonObject = jsonMapper.readJson();
        List<MedicalRecord> medicalRecords = jsonObject.getMedicalrecords();

        // A medical record already in data file may not be saved.
        boolean saveAuthorized =
                (medicalRecords.stream()
                        .noneMatch(medicalRecordOfList -> medicalRecordOfList.getFirstName().equals(medicalRecord.getFirstName()) & medicalRecordOfList.getLastName().equals(medicalRecord.getLastName())));

        if (saveAuthorized) {
            jsonObject.getMedicalrecords().add(medicalRecord);
            jsonMapper.writeJson(jsonObject);
            return medicalRecord;
        } else {
            throw new AlreadyInDataFileException("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " already exists in data file.");
        }

    }

    /**
     * Updates a medicalRecord in data file
     *
     * @param medicalRecord The medical record to be updated.
     * @return If the medical record to be updated isn't in data base, then "null" is returned, otherwise, the medical record updated is returned.
     */
    @Override
    public MedicalRecord update(MedicalRecord medicalRecord) {

        JsonObject jsonObject = jsonMapper.readJson();
        List<MedicalRecord> medicalRecords = jsonObject.getMedicalrecords();

        boolean updateOk = medicalRecords.stream()
                .filter(medicalRecordOfList -> medicalRecordOfList.getFirstName().equals(medicalRecord.getFirstName()) & medicalRecordOfList.getLastName().equals(medicalRecord.getLastName()))
                .peek(personOfList -> {
                    personOfList.setFirstName(medicalRecord.getFirstName());
                    personOfList.setLastName(medicalRecord.getLastName());
                    personOfList.setBirthdate(medicalRecord.getBirthdate());
                    personOfList.setMedications(medicalRecord.getMedications());
                    personOfList.setAllergies(medicalRecord.getAllergies());
                })
                .count() == 1;

        if (updateOk) {
            jsonObject.setMedicalrecords(medicalRecords);
            jsonMapper.writeJson(jsonObject);
        } else {
            throw new NotFoundInDataFileException("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " doesn't exist in data file.");
        }

        return medicalRecord;
    }

    /**
     * Removes a medicalRecord in data file
     *
     * @param medicalRecord The medical record to be removed.
     * @return true if the removal is ok, false if the removal is ko
     */
    @Override
    public boolean remove(MedicalRecord medicalRecord) {

        JsonObject jsonObject = jsonMapper.readJson();
        List<MedicalRecord> medicalRecords = jsonObject.getMedicalrecords();

        boolean removeOk =
                (medicalRecords.stream()
                        .anyMatch(medicalRecordOfList -> medicalRecordOfList.getLastName().equals(medicalRecord.getLastName()) & medicalRecordOfList.getFirstName().equals(medicalRecord.getFirstName())));

        if (removeOk) {
            medicalRecords.remove(medicalRecord);
            jsonObject.setMedicalrecords(medicalRecords);
            jsonMapper.writeJson(jsonObject);
        } else {
            throw new NotFoundInDataFileException("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " doesn't exist in data file.");
        }
        return true;
    }
}
