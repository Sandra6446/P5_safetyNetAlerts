package com.openclassrooms.safetyNetAlerts.dao;

import com.openclassrooms.safetyNetAlerts.model.ObjectFromJson;
import com.openclassrooms.safetyNetAlerts.model.Person;
import com.openclassrooms.safetyNetAlerts.util.JsonMapper;
import com.openclassrooms.safetyNetAlerts.exceptions.AlreadyInDataFileException;
import com.openclassrooms.safetyNetAlerts.exceptions.NotFoundInDataFileException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

/**
 * To add, update and remove persons data in data file.
 */

@Component
public class PersonDAO implements IDataInJsonDao<Person> {

    private static final Logger logger = LogManager.getLogger(PersonDAO.class);

    private JsonMapper jsonMapper = new JsonMapper();

    @Override
    public List<Person> getAll() {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Person> persons = objectFromJson.getPersons();

        return persons;
    }

    public List<Person> getByAddress(String address) {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Person> persons = objectFromJson.getPersons()
                .stream()
                .filter(person -> person.getAddress().equals(address))
                .collect(Collectors.toList());
        return persons;
    }

    public Person getByName(String firstName, String lastName) {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Person> persons = objectFromJson.getPersons()
                .stream()
                .filter(person -> person.getFirstName().equals(firstName) & person.getLastName().equals(lastName))
                .collect(Collectors.toList());
        return persons.get(0);
    }

    /**
     * @param person The person to be added.
     * @return If the person to be added is already in data base, then "null" is returned, otherwise, the person added is returned.
     */
    @Override
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
    @Override
    public Person update(Person person) {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Person> persons = objectFromJson.getPersons();

        boolean updateOk = persons.stream()
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
    @Override
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
