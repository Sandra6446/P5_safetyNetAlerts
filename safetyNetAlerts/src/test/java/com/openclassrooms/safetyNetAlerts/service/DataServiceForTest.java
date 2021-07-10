package com.openclassrooms.safetyNetAlerts.service;

import com.openclassrooms.safetyNetAlerts.model.*;
import lombok.Data;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class DataServiceForTest {

    private final MyPerson childMyMap1 = new MyPerson("John","Boyd","841-874-6513","jaboyd@email.com",5,
            new MyMedicalRecord(new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")), new ArrayList<>(Collections.singletonList("nillacilan"))));

    private final MyPerson adultMyMap1 = new MyPerson("Jacob","Boyd","841-874-6513","jaboyd@email.com",35,
            new MyMedicalRecord(new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")), new ArrayList<>(Collections.singletonList("nillacilan"))));

    private final MyPerson adultMyMap2 = new MyPerson("Jonanathan","Marrack","841-874-6544","drk@email.com",35,
            new MyMedicalRecord(new ArrayList<>(), new ArrayList<>(Collections.singletonList("peanut"))));

    private final Address addressMyMap1 = new Address("1509 Culver St","Culver",97451);

    private final Address addressMyMap2 = new Address("29 15th St","Culver",97451);

    public List<House> buildHousesForTest() {
        List<House> houses = new ArrayList<>();
        houses.add(new House(addressMyMap1.getStreet(), addressMyMap1.getCity(), addressMyMap1.getZip(),new ArrayList<>(Arrays.asList(childMyMap1, adultMyMap1))));
        houses.add(new House(addressMyMap2.getStreet(), addressMyMap2.getCity(), addressMyMap2.getZip(),new ArrayList<>(Collections.singletonList(adultMyMap2))));
        return houses;
    }

    public List<Family> buildFamiliesForTest() {
        List<Family> families = new ArrayList<>();
        families.add(new Family(addressMyMap1.getStreet(), addressMyMap1.getCity(), addressMyMap1.getZip(),new ArrayList<>(Collections.singletonList(childMyMap1)),new ArrayList<>(Collections.singletonList(adultMyMap1))));
        families.add(new Family(addressMyMap2.getStreet(), addressMyMap2.getCity(), addressMyMap2.getZip(),new ArrayList<>(),new ArrayList<>(Collections.singletonList(adultMyMap2))));
        return families;
    }

    public List<MyMap> buildMapsForTest() {
        List<MyMap> maps = new ArrayList<>();
        maps.add(new MyMap("1",1,1,this.buildHousesForTest()));
        maps.add(new MyMap("2",1,0,new ArrayList<>(Collections.singletonList(new House(addressMyMap2.getStreet(), addressMyMap2.getCity(), addressMyMap2.getZip(), new ArrayList<>(Collections.singletonList(adultMyMap2)))))));
        return maps;
    }

    public List<Person> buildPersonsForTest() {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("John","Boyd","1509 Culver St","Culver",97451,"841-874-6513","jaboyd@email.com"));
        persons.add(new Person("Jacob","Boyd","1509 Culver St","Culver",97451,"841-874-6513","jaboyd@email.com"));
        persons.add(new Person("Jonanathan","Marrack","29 15th St","Culver",97451,"841-874-6544","drk@email.com"));
        return persons;
    }

    public List<MedicalRecord> buildMedicalRecords() {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(new MedicalRecord("John","Boyd","01/01/2016",new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")),new ArrayList<>(Collections.singletonList("nillacilan"))));
        medicalRecords.add(new MedicalRecord("Jacob","Boyd","01/01/1986",new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")),new ArrayList<>(Collections.singletonList("nillacilan"))));
        medicalRecords.add(new MedicalRecord("Jonanathan","Marrack","01/01/1986",new ArrayList<>(), new ArrayList<>(Collections.singletonList("peanut"))));
        return medicalRecords;
    }
}
