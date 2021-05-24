package dao;

import exceptions.AlreadyInDataFileException;
import exceptions.NotFoundInDataFileException;
import model.ObjectFromJson;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import util.JsonMapper;

import java.util.List;

/**
 * To add, update and remove persons data in data file.
 */

@Component
@Import(value = JsonMapper.class)
public class PersonDAO {

    private static final Logger logger = LogManager.getLogger(PersonDAO.class);

    @Autowired
    private JsonMapper jsonMapper;

    /**
     * @param person The person to be added.
     * @return If the person to be added is already in data base, then "null" is returned, otherwise, the person added is returned.
     */
    public Person save(Person person) throws AlreadyInDataFileException {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Person> persons = objectFromJson.getPersons();

        // A person already in data file may not be saved.
        boolean saveAuthorized =
                (persons.stream()
                        .noneMatch(personOfList -> personOfList.getFirstName().equals(person.getFirstName()) & personOfList.getLastName().equals(person.getLastName())));

        if (saveAuthorized) {
            objectFromJson.getPersons().add(person);
            jsonMapper.writeJson(objectFromJson);
            return person;
        } else {
            throw new AlreadyInDataFileException(person.getFirstName() + " " + person.getLastName() + " already exists in data file.");
        }

    }

    /**
     * @param person The person to be updated.
     * @return If the person to be updated isn't in data base, then "null" is returned, otherwise, the person updated is returned.
     */
    public Person update(Person person) {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Person> persons = objectFromJson.getPersons();

        boolean updateOk = persons.stream()
                .filter(personOfList -> personOfList.getFirstName().equals(person.getFirstName()) & personOfList.getLastName().equals(person.getLastName()))
                .peek(personOfList -> {
                    personOfList.setAddress(person.getAddress());
                    personOfList.setZip(person.getZip());
                    personOfList.setCity(person.getCity());
                    personOfList.setPhone(person.getPhone());
                    personOfList.setEmail(person.getEmail());
                })
                .count() == 1;

        if (updateOk) {
            objectFromJson.setPersons(persons);
            jsonMapper.writeJson(objectFromJson);
        } else {
            throw new NotFoundInDataFileException(person.getFirstName() + " " + person.getLastName() + " doesn't exist in data file.");
        }

        return person;
    }

    /**
     * @param person The person to be removed.
     * @return true if the removal is ok, false if the removal is ko
     */
    public boolean remove(Person person) {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Person> persons = objectFromJson.getPersons();

        boolean removeOk =
                (persons.stream()
                        .anyMatch(personOfList -> personOfList.getLastName().equals(person.getLastName()) & personOfList.getFirstName().equals(person.getFirstName())));

        if (removeOk) {
            persons.remove(person);
            objectFromJson.setPersons(persons);
            jsonMapper.writeJson(objectFromJson);
        } else {
            throw new NotFoundInDataFileException(person.getFirstName() + " " + person.getLastName() + " doesn't exist in data file.");
        }
        return true;
    }
}
