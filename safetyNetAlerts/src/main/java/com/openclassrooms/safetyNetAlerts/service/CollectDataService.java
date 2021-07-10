package com.openclassrooms.safetyNetAlerts.service;

import com.openclassrooms.safetyNetAlerts.dao.FirestationsDAO;
import com.openclassrooms.safetyNetAlerts.dao.MedicalRecordsDAO;
import com.openclassrooms.safetyNetAlerts.dao.PersonsDAO;
import com.openclassrooms.safetyNetAlerts.model.*;
import com.openclassrooms.safetyNetAlerts.util.UtilsHouse;
import com.openclassrooms.safetyNetAlerts.util.UtilsMyPerson;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Regroups several construction and calculation classes
 */
@Service
public class CollectDataService {

    @Autowired
    private PersonsDAO personsDAO;

    @Autowired
    private FirestationsDAO firestationsDAO;

    @Autowired
    private MedicalRecordsDAO medicalRecordsDAO;

    /**
     * Collects the data from json file into House
     *
     * @return A list of House
     */
    public List<House> buildHouses() {
        List<House> houses = new ArrayList<>();

        for (Person person : personsDAO.getAll()) {
            UtilsMyPerson.putMyPersonInAHouse(houses, this.convertToAPersonne(person), new Address(person.getAddress(), person.getCity(), person.getZip()));
        }

        return houses;
    }

    /**
     * Collects the data from json file into Family
     *
     * @return A list of Family
     */
    public List<Family> buildFamilies() {
        List<Family> families = new ArrayList<>();

        for (Person person : personsDAO.getAll()) {
            UtilsMyPerson.putMyPersonInAFamily(families, this.convertToAPersonne(person), new Address(person.getAddress(), person.getCity(), person.getZip()));
        }

        return families;
    }

    /**
     * Collects the data from json file into MyMap
     *
     * @return A list of MyMap
     */
    public List<MyMap> buildMyMaps() {
        List<MyMap> myMaps = new ArrayList<>();
        List<House> houses = buildHouses();
        for (Firestation firestation : firestationsDAO.getAll()) {
            UtilsHouse.putHouseInMap(myMaps, firestation, houses);
        }
        return myMaps;
    }

    /**
     * Changes a Person into a MyPerson
     * @param person The person to be converted
     * @return A MyPerson
     */
    private MyPerson convertToAPersonne(Person person) {

        MyPerson myPerson = new MyPerson();
        myPerson.setFirstName(person.getFirstName());
        myPerson.setLastName(person.getLastName());
        myPerson.setEmail(person.getEmail());
        myPerson.setPhone(person.getPhone());

        MedicalRecord medicalRecord = medicalRecordsDAO.getAll()
                .stream()
                .filter(medicalRecordOfList -> medicalRecordOfList.getFirstName().equals(person
                        .getFirstName()) & medicalRecordOfList.getLastName().equals(person.getLastName()))
                .collect(Collectors.toList())
                .get(0);

        myPerson.setAge(getAge(medicalRecord.getBirthdate()));
        myPerson.setMyMedicalRecord(new MyMedicalRecord(medicalRecord));

        return myPerson;
    }

    /**
     * Calculates an age with a birthdate
     * @param birthdate The birthdate used to calculate the age
     * @return An age
     */
    private int getAge(String birthdate) {
        DateTime today = new DateTime();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy").withLocale(Locale.FRENCH);
        DateTime date = formatter.parseDateTime(birthdate);
        Years years = Years.yearsBetween(date, today);
        return years.getYears();
    }

}
