package com.openclassrooms.safetyNetAlerts.dao;

import com.openclassrooms.safetyNetAlerts.exceptions.AlreadyInDataFileException;
import com.openclassrooms.safetyNetAlerts.exceptions.NotFoundInDataFileException;
import com.openclassrooms.safetyNetAlerts.model.JsonObject;
import com.openclassrooms.safetyNetAlerts.model.Person;
import com.openclassrooms.safetyNetAlerts.util.JsonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Reads and update the list of persons in json data file
 */

@Component
public class PersonsDAO implements IDataInJsonDao<Person> {

    private static final Logger logger = LogManager.getLogger(PersonsDAO.class);

    private final JsonMapper jsonMapper = new JsonMapper();

    /**
     * Reads the list of persons in json data file
     *
     * @return A list of all the persons from data file
     */
    @Override
    public List<Person> getAll() {

        JsonObject jsonObject = jsonMapper.readJson();
        List<Person> people = jsonObject.getPersons();

        return people;
    }

    /**
     * Adds a person in json data file
     *
     * @param person The person to be added.
     * @return If the person to be added is already in data base, then "null" is returned, otherwise, the person added is returned.
     */
    @Override
    public Person save(Person person) throws AlreadyInDataFileException {

        JsonObject jsonObject = jsonMapper.readJson();
        List<Person> people = jsonObject.getPersons();

        // A person already in data file may not be saved.
        boolean saveAuthorized =
                (people.stream()
                        .noneMatch(personOfList -> personOfList.getFirstName().equals(person.getFirstName()) & personOfList.getLastName().equals(person.getLastName())));

        if (saveAuthorized) {
            jsonObject.getPersons().add(person);
            jsonMapper.writeJson(jsonObject);
            return person;
        } else {
            throw new AlreadyInDataFileException(person.getFirstName() + " " + person.getLastName() + " already exists in data file.");
        }

    }

    /**
     * Updates a person in data file
     *
     * @param person The person to be updated.
     * @return If the person to be updated isn't in data base, then "null" is returned, otherwise, the person updated is returned.
     */
    @Override
    public Person update(Person person) {

        JsonObject jsonObject = jsonMapper.readJson();
        List<Person> people = jsonObject.getPersons();

        boolean updateOk = people.stream()
                .filter(personOfList -> personOfList.getFirstName().equals(person.getFirstName()) & personOfList.getLastName().equals(person.getLastName()))
                .peek(personOfList -> {
                    personOfList.setAddress(person.getAddress());
                    personOfList.setCity(person.getCity());
                    personOfList.setZip(person.getZip());
                    personOfList.setPhone(person.getPhone());
                    personOfList.setEmail(person.getEmail());
                })
                .count() == 1;

        if (updateOk) {
            jsonObject.setPersons(people);
            jsonMapper.writeJson(jsonObject);
        } else {
            throw new NotFoundInDataFileException(person.getFirstName() + " " + person.getLastName() + " doesn't exist in data file.");
        }

        return person;
    }

    /**
     * Deletes a person in data file
     *
     * @param person The person to be removed.
     * @return true if the removal is ok, false if the removal is ko
     */
    @Override
    public boolean remove(Person person) {

        JsonObject jsonObject = jsonMapper.readJson();
        List<Person> people = jsonObject.getPersons();

        boolean removeOk =
                (people.stream()
                        .anyMatch(personOfList -> personOfList.getLastName().equals(person.getLastName()) & personOfList.getFirstName().equals(person.getFirstName())));

        if (removeOk) {
            people.remove(person);
            jsonObject.setPersons(people);
            jsonMapper.writeJson(jsonObject);
        } else {
            throw new NotFoundInDataFileException(person.getFirstName() + " " + person.getLastName() + " doesn't exist in data file.");
        }
        return true;
    }
}
