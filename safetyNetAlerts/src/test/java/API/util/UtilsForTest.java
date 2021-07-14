package API.util;

import API.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UtilsForTest {

    /*
        A list of three MyPerson :
        child :
        1. firstName : "John"
           lastName : "Boyd"
           phone : "841-874-6512"
           email : "jaboyd@email.com"
           age : 5
           myMedicalRecord :    medications : ["aznol:350mg", "hydrapermazol:100mg"]
                                allergies : ["nillacilan"]
        2. firstName : "Jacob"
           lastName : "Boyd"
           phone : "841-874-6512"
           email : "jaboyd@email.com"
           age : 35
           myMedicalRecord :    medications : ["aznol:350mg", "hydrapermazol:100mg"]
                                allergies : ["nillacilan"]
        3 : firstName : "Jonathan"
            lastName : "Marrack"
            phone : "841-874-6513"
            email : "drk@email.com"
            age : 35
            myMedicalRecord :    medications : []
                                 allergies : ["peanut"]
    */
    public static List<MyPerson> myPersonsForTest() {
        MyPerson child = new MyPerson("John", "Boyd", "841-874-6512", "jaboyd@email.com", 5,
                new MyMedicalRecord(new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")), new ArrayList<>(Collections.singletonList("nillacilan"))));
        MyPerson adult1 = new MyPerson("Jacob", "Boyd", "841-874-6512", "jaboyd@email.com", 35,
                new MyMedicalRecord(new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")), new ArrayList<>(Collections.singletonList("nillacilan"))));
        MyPerson adult2 = new MyPerson("Jonathan", "Marrack", "841-874-6513", "drk@email.com", 35,
                new MyMedicalRecord(new ArrayList<>(), new ArrayList<>(Collections.singletonList("peanut"))));
        return new ArrayList<>(Arrays.asList(child, adult1, adult2));
    }

    /*
        A list of two Address :
        1 : street : "1509 Culver St"
             city : "Culver"
             zip : 97451
        2 : street : "29 15th St"
             city : "Culver"
             zip : 97451
    */
    public static List<Address> addressesForTest() {
        Address address1 = new Address("1509 Culver St", "Culver", 97451);
        Address address2 = new Address("29 15th St", "Culver", 97451);
        return new ArrayList<>(Arrays.asList(address1, address2));
    }

    /*
         A list of two House :
         1 : street : "1509 Culver St"
             city : "Culver"
             zip : 97451
             residents : myPersonsForTest n°1 et 2
         2 : street : "29 15th St"
             city : "Culver"
             zip : 97451
             residents : myPersonsForTest n°3
     */
    public static List<House> housesForTest() {
        List<House> houses = new ArrayList<>();
        houses.add(new House(addressesForTest().get(0).getStreet(), addressesForTest().get(0).getCity(), addressesForTest().get(0).getZip(), myPersonsForTest().subList(0,2)));
        houses.add(new House(addressesForTest().get(1).getStreet(), addressesForTest().get(1).getCity(), addressesForTest().get(1).getZip(), myPersonsForTest().subList(2,3)));
        return houses;
    }

    /*
        A list of two Family :
        1 : street : "1509 Culver St"
            city : "Culver"
            zip : 97451
            children : myPersonsForTest n°1
            adults : myPersonsForTest n°2
        2 : street : "29 15th St"
            city : "Culver"
            zip : 97451
            children : []
            adults : myPersonsForTest n°3
    */
    public static List<Family> familiesForTest() {
        List<Family> families = new ArrayList<>();
        families.add(new Family(addressesForTest().get(0).getStreet(), addressesForTest().get(0).getCity(), addressesForTest().get(0).getZip(), myPersonsForTest().subList(0,1), myPersonsForTest().subList(1,2)));
        families.add(new Family(addressesForTest().get(1).getStreet(), addressesForTest().get(1).getCity(), addressesForTest().get(1).getZip(), new ArrayList<>(), myPersonsForTest().subList(2,3)));
        return families;
    }

    /*
        A list of two MyMap :
        1 : station : "1"
            nbOfAdults : 2
            nbOfChildren : 1
            houses : housesForTest
        2 : station : "2"
            nbOfAdults : 1
            nbOfChildren : 0,
            houses : housesForTest n°2
    */
    public static List<MyMap> myMapsForTest() {
        List<MyMap> maps = new ArrayList<>();
        maps.add(new MyMap("1", 2, 1, housesForTest()));
        maps.add(new MyMap("2", 1, 0, housesForTest().subList(1, 2)));
        return maps;
    }

    public static JsonObject jsonObjectForIT() {
        JsonObject jsonObject = new JsonObject();
        Person person1 = new Person("John", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6512", "jaboyd@email.com");
        Person person2 = new Person("Jacob", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6512", "jaboyd@email.com");
        Person person3 = new Person("Jonathan", "Marrack", "29 15th St", "Culver", 97451, "841-874-6513", "drk@email.com");
        jsonObject.setPersons(new ArrayList<>(Arrays.asList(person1, person2, person3)));
        Firestation firestation1 = new Firestation("1509 Culver St", "1");
        Firestation firestation2 = new Firestation("29 15th St", "2");
        jsonObject.setFirestations(new ArrayList<>(Arrays.asList(firestation1, firestation2)));
        MedicalRecord medicalRecord1 = new MedicalRecord("John", "Boyd", "01/01/2016", new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")), new ArrayList<>(Collections.singletonList("nillacilan")));
        MedicalRecord medicalRecord2 = new MedicalRecord("Jacob", "Boyd", "01/01/1986", new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")), new ArrayList<>(Collections.singletonList("nillacilan")));
        MedicalRecord medicalRecord3 = new MedicalRecord("Jonathan", "Marrack", "01/01/1986", new ArrayList<>(), new ArrayList<>(Collections.singletonList("peanut")));
        jsonObject.setMedicalrecords(new ArrayList<>(Arrays.asList(medicalRecord1, medicalRecord2, medicalRecord3)));
        return jsonObject;
    }
}
