package com.safetynetalert.microserviceperson.dao;

import model.ObjectFromJson;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import util.JsonMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * To add, update and remove persons data in data file.
 */

@Repository
@Import(value = JsonMapper.class)
public class PersonDAO {

    /**
     * For error handling.
     */
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PersonDAO.class);

    /**
     * Object that reads and writes in data file.
     */
    @Autowired
    private JsonMapper jsonMapper;

    /**
     * @param person Parameters of the person to be saved.
     * @return Person saved.
     */
    public Person save(Person person) {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Person> persons = objectFromJson.getPersons();

        // A person already in data file may not be saved.
        boolean saveAuthorized =
                (persons.stream()
                        .filter(personOfList -> personOfList.getFirstName().equals(person.getFirstName()) & personOfList.getLastName().equals(person.getLastName()))
                        .limit(1)
                        .count() == 0);

        if (saveAuthorized) {
            objectFromJson.getPersons().add(person);
            jsonMapper.writeJson(objectFromJson);
            return person;
        } else {
            logger.error(person.getFirstName() + " " + person.getLastName() + " already exists in data file.");
            return null;
        }

    }

    /**
     * @param person Parameters of the person to be updated.
     * @return Person updated.
     */
    public Person update(Person person) {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Person> persons = objectFromJson.getPersons();

        Person personUpdated = persons.stream()
                .filter(personOfList -> personOfList.getFirstName().equals(person.getFirstName()) & personOfList.getLastName().equals(person.getLastName()))
                .peek(personOfList -> {
                    personOfList.setAddress(person.getAddress());
                    personOfList.setZip(person.getZip());
                    personOfList.setCity(person.getCity());
                    personOfList.setPhone(person.getPhone());
                    personOfList.setEmail(person.getEmail());
                })
                .limit(1)
                .collect(Collectors.toList())
                .get(0);

        if (personUpdated != null) {
            objectFromJson.setPersons(persons);
            jsonMapper.writeJson(objectFromJson);
        } else {
            logger.error(person.getFirstName() + " " + person.getLastName() + " doesn't exist in data file.");
        }

        return personUpdated;
    }

    /**
     * @param person Parameters of the person to be removed.
     * @return Status of the removal.
     */
    public boolean remove(Person person) {

        ObjectFromJson objectFromJson = jsonMapper.readJson();
        List<Person> persons = objectFromJson.getPersons();
        boolean removeOk = false;

        for (Person personOfList : persons) {
            if (personOfList.getLastName().equals(person.getLastName()) & personOfList.getFirstName().equals(person.getFirstName())) {
                persons.remove(personOfList);
                objectFromJson.setPersons(persons);
                jsonMapper.writeJson(objectFromJson);
                removeOk = true;
                break;
            } else {
                logger.error(person.getFirstName() + " " + person.getLastName() + " doesn't exists in data file.");
            }
        }
        return removeOk;
    }
}
