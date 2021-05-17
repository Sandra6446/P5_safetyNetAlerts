package dao;

import exceptions.AlreadyInDataFileException;
import exceptions.NotFoundInDataFileException;
import model.Firestation;
import model.ObjectFromJson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import util.JsonMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Import(value = JsonMapper.class)
public class FirestationDAO {

    private static final Logger logger = LogManager.getLogger(PersonDAO.class);

    @Autowired
    private JsonMapper jsonMapper;

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

    public Firestation update(Firestation firestation) {
        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Firestation> firestations = objectFromJson.getFirestations();

        boolean updateOk =
                (firestations.stream()
                        .filter(firestationOfList -> firestationOfList.getAddress().equals(firestation.getAddress()))
                        .peek(firestationOfList -> {
                            firestationOfList.setStation(firestation.getStation());
                        })
                        .count() == 1);

        if (updateOk) {
            objectFromJson.setFirestations(firestations);
            jsonMapper.writeJson(objectFromJson);
        } else {
            throw new NotFoundInDataFileException("There is no firestation at " + firestation.getAddress() + " in data file.");
        }

        return firestation;
    }

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
