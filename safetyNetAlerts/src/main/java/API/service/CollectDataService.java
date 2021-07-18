package API.service;

import API.dao.FirestationsDAO;
import API.dao.MedicalRecordsDAO;
import API.dao.PersonsDAO;
import API.model.*;
import API.util.UtilsHouse;
import API.util.UtilsMyPerson;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CollectDataService {

    /**
     * The PersonsDAO used to read in json data file
     */
    @Autowired
    private final PersonsDAO personsDAO;

    /**
     * The FirestationsDAO used to read in json data file
     */
    @Autowired
    private final FirestationsDAO firestationsDAO;

    /**
     * The MedicalRecordsDAO used to read in json data file
     */
    @Autowired
    private final MedicalRecordsDAO medicalRecordsDAO;

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
            UtilsHouse.putHouseInMyMap(myMaps, firestation, houses);
        }
        return myMaps;
    }

    /**
     * Changes a Person into a MyPerson
     *
     * @param person The Person to be converted
     * @return A MyPerson
     */
    public MyPerson convertToAPersonne(Person person) {

        MyPerson myPerson = new MyPerson();
        myPerson.setFirstName(person.getFirstName());
        myPerson.setLastName(person.getLastName());
        myPerson.setEmail(person.getEmail());
        myPerson.setPhone(person.getPhone());

        MedicalRecord medicalRecord = medicalRecordsDAO.getAll()
                .stream()
                .filter(medicalRecordOfList -> medicalRecordOfList.getFirstName().equalsIgnoreCase(person
                        .getFirstName()) & medicalRecordOfList.getLastName().equalsIgnoreCase(person.getLastName()))
                .collect(Collectors.toList())
                .get(0);

        myPerson.setAge(getAge(medicalRecord.getBirthdate()));
        myPerson.setMyMedicalRecord(new MyMedicalRecord(medicalRecord));

        return myPerson;
    }

    /**
     * Calculates an age with a birthdate
     *
     * @param birthdate The birthdate used to calculate the age
     * @return An age
     */
    public int getAge(String birthdate) {
        DateTime today = new DateTime();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy").withLocale(Locale.FRENCH);
        DateTime date = formatter.parseDateTime(birthdate);
        Years years = Years.yearsBetween(date, today);
        return years.getYears();
    }

}
