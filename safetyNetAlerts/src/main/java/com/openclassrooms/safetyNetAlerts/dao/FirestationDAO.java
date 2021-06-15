package dao;

import util.JsonMapper;
import exceptions.AlreadyInDataFileException;
import exceptions.NotFoundInDataFileException;
import model.Firestation;
import model.ObjectFromJson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * To add, update and remove firestations data in data file.
 */

@Component
public class FirestationDAO implements IDataInJsonDao<Firestation> {

    private static final Logger logger = LogManager.getLogger(PersonDAO.class);

    private JsonMapper jsonMapper = new JsonMapper();

    @Override
    public List<Firestation> getAll() {
        ObjectFromJson objectFromJson = jsonMapper.readJson();
        return objectFromJson.getFirestations();
    }

    public List<Firestation> getByAddress(String address) {
        ObjectFromJson objectFromJson = jsonMapper.readJson();
        return objectFromJson.getFirestations()
                .stream()
                .filter(firestation -> firestation.getAddress().equals(address))
                .collect(Collectors.toList());
    }

    public List<Firestation> getByStation(String station) {
        ObjectFromJson objectFromJson = jsonMapper.readJson();
        return objectFromJson.getFirestations()
                .stream()
                .filter(firestation -> firestation.getStation().equals(station))
                .collect(Collectors.toList());
    }

    /**
     * @param firestation The firestation to be added.
     * @return If the firestation to be added is already in data base, then "null" is returned, otherwise, the firestation added is returned.
     */
    @Override
    public Firestation save(Firestation firestation) throws AlreadyInDataFileException {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Firestation> firestations = objectFromJson.getFirestations();

        // A firestation already in data file may not be saved.
        boolean saveAuthorized =
                (firestations.stream()
                        .noneMatch(firestationOfList -> firestationOfList.getAddress().equals(firestation.getAddress()) & firestationOfList.getStation().equals(firestation.getStation())));

        if (saveAuthorized) {
            objectFromJson.getFirestations().add(firestation);
            jsonMapper.writeJson(objectFromJson);
            return firestation;
        } else {
            throw new AlreadyInDataFileException("Firestation number " + firestation.getStation() + " at " + firestation.getAddress() + " already exists in data file.");
        }

    }

    /**
     * @param firestation The firestation to be updated.
     * @return If the firestation to be updated isn't in data base, then "null" is returned, otherwise, the firestation updated is returned.
     */
    @Override
    public Firestation update(Firestation firestation) {
        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Firestation> firestations = objectFromJson.getFirestations();

        boolean updateOk =
                (firestations.stream()
                        .filter(firestationOfList -> firestationOfList.getAddress().equals(firestation.getAddress()))
                        .peek(firestationOfList -> firestationOfList.setStation(firestation.getStation()))
                        .count() == 1);

        if (updateOk) {
            objectFromJson.setFirestations(firestations);
            jsonMapper.writeJson(objectFromJson);
        } else {
            throw new NotFoundInDataFileException("There is no firestation at " + firestation.getAddress() + " in data file.");
        }

        return firestation;
    }

    /**
     * @param firestation The firestation to be removed.
     * @return true if the removal is ok, false if the removal is ko
     */
    @Override
    public boolean remove(Firestation firestation) {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Firestation> firestations = objectFromJson.getFirestations();

        boolean removeOk =
                (firestations.stream()
                        .anyMatch(firestationOfList -> firestationOfList.getAddress().equals(firestation.getAddress())));

        if (removeOk) {
            firestations.remove(firestation);
            objectFromJson.setFirestations(firestations);
            jsonMapper.writeJson(objectFromJson);
        } else {
            throw new NotFoundInDataFileException("There is no firestation at " + firestation.getAddress() + " in data file.");
        }

        return true;
    }
}
