package API.dao;

import API.exceptions.AlreadyInDataFileException;
import API.exceptions.NotFoundInDataFileException;
import API.model.Firestation;
import API.model.JsonObject;
import API.util.JsonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Reads and updates the list of firestations in json data file.
 */

@Component
public class FirestationsDAO implements IDataInJsonDao<Firestation> {

    private static final Logger logger = LogManager.getLogger(FirestationsDAO.class);

    @Autowired
    private JsonMapper jsonMapper;

    /**
     * Reads the list of firestations in json data file
     *
     * @return A list of all the firestations from data file
     */
    @Override
    public List<Firestation> getAll() {
        JsonObject jsonObject = jsonMapper.readJson();
        return jsonObject.getFirestations();
    }

    /**
     * Adds a firestation in json data file
     *
     * @param firestation The firestation to be added.
     * @return If the firestation to be added is already in data base, then "null" is returned, otherwise, the firestation added is returned.
     */
    @Override
    public Firestation save(Firestation firestation) throws AlreadyInDataFileException {

        JsonObject jsonObject = jsonMapper.readJson();
        List<Firestation> firestations = jsonObject.getFirestations();

        // A firestation already in data file may not be saved.
        boolean saveAuthorized =
                (firestations.stream()
                        .noneMatch(firestationOfList -> firestationOfList.getAddress().equalsIgnoreCase(firestation.getAddress()) & firestationOfList.getStation().equals(firestation.getStation())));

        if (saveAuthorized) {
            jsonObject.getFirestations().add(firestation);
            jsonMapper.writeJson(jsonObject);
            return firestation;
        } else {
            throw new AlreadyInDataFileException("Firestation number " + firestation.getStation() + " at " + firestation.getAddress() + " already exists in data file.");
        }

    }

    /**
     * Updates a firestation in data file
     *
     * @param firestation The firestation to be updated.
     * @return If the firestation to be updated isn't in data base, then "null" is returned, otherwise, the firestation updated is returned.
     */
    @Override
    public Firestation update(Firestation firestation) {
        JsonObject jsonObject = jsonMapper.readJson();
        List<Firestation> firestations = jsonObject.getFirestations();

        boolean updateOk =
                (firestations.stream()
                        .filter(firestationOfList -> firestationOfList.getAddress().equalsIgnoreCase(firestation.getAddress()))
                        .peek(firestationOfList -> firestationOfList.setStation(firestation.getStation()))
                        .count() > 0);

        if (updateOk) {
            jsonObject.setFirestations(firestations);
            jsonMapper.writeJson(jsonObject);
        } else {
            throw new NotFoundInDataFileException("There is no firestation at " + firestation.getAddress() + " in data file.");
        }

        return firestation;
    }

    /**
     * Removes a firestation in data file
     *
     * @param firestation The firestation to be removed.
     * @return true if the removal is ok, false if the removal is ko
     */
    @Override
    public boolean remove(Firestation firestation) {

        JsonObject jsonObject = jsonMapper.readJson();
        List<Firestation> firestations = jsonObject.getFirestations();

        boolean removeOk =
                (firestations.stream()
                        .anyMatch(firestationOfList -> firestationOfList.getAddress().equalsIgnoreCase(firestation.getAddress()) && firestationOfList.getStation().equals(firestation.getStation())));

        if (removeOk) {
            firestations.remove(firestation);
            jsonObject.setFirestations(firestations);
            jsonMapper.writeJson(jsonObject);
        } else {
            throw new NotFoundInDataFileException("There is no firestation at " + firestation.getAddress() + " in data file.");
        }

        return true;
    }
}
