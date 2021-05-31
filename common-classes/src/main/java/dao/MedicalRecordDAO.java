package dao;

import exceptions.AlreadyInDataFileException;
import exceptions.NotFoundInDataFileException;
import model.MedicalRecord;
import model.ObjectFromJson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import util.JsonMapper;

import java.util.List;

/**
 * To add, update and remove medical records data in data file.
 */

@Component
@Import(value = JsonMapper.class)
public class MedicalRecordDAO {

    private static final Logger logger = LogManager.getLogger(MedicalRecordDAO.class);

    @Autowired
    private JsonMapper jsonMapper;

    /**
     * @param medicalRecord The medical record to be added.
     * @return If the medical record to be added is already in data base, then "null" is returned, otherwise, the medical record added is returned.
     */
    public MedicalRecord save(MedicalRecord medicalRecord) throws AlreadyInDataFileException {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<MedicalRecord> medicalRecords = objectFromJson.getMedicalrecords();

        // A medical record already in data file may not be saved.
        boolean saveAuthorized =
                (medicalRecords.stream()
                        .noneMatch(medicalRecordOfList -> medicalRecordOfList.getFirstName().equals(medicalRecord.getFirstName()) & medicalRecordOfList.getLastName().equals(medicalRecord.getLastName())));

        if (saveAuthorized) {
            objectFromJson.getMedicalrecords().add(medicalRecord);
            jsonMapper.writeJson(objectFromJson);
            return medicalRecord;
        } else {
            throw new AlreadyInDataFileException("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " already exists in data file.");
        }

    }

    /**
     * @param medicalRecord The medical record to be updated.
     * @return If the medical record to be updated isn't in data base, then "null" is returned, otherwise, the medical record updated is returned.
     */
    public MedicalRecord update(MedicalRecord medicalRecord) {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<MedicalRecord> medicalRecords = objectFromJson.getMedicalrecords();

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
            objectFromJson.setMedicalrecords(medicalRecords);
            jsonMapper.writeJson(objectFromJson);
        } else {
            throw new NotFoundInDataFileException("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " doesn't exist in data file.");
        }

        return medicalRecord;
    }

    /**
     * @param medicalRecord The medical record to be removed.
     * @return true if the removal is ok, false if the removal is ko
     */
    public boolean remove(MedicalRecord medicalRecord) {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<MedicalRecord> medicalRecords = objectFromJson.getMedicalrecords();

        boolean removeOk =
                (medicalRecords.stream()
                        .anyMatch(medicalRecordOfList -> medicalRecordOfList.getLastName().equals(medicalRecord.getLastName()) & medicalRecordOfList.getFirstName().equals(medicalRecord.getFirstName())));

        if (removeOk) {
            medicalRecords.remove(medicalRecord);
            objectFromJson.setMedicalrecords(medicalRecords);
            jsonMapper.writeJson(objectFromJson);
        } else {
            throw new NotFoundInDataFileException("The medical record of " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " doesn't exist in data file.");
        }
        return true;
    }
}
