package com.openclassrooms.safetyNetAlerts.service;

import com.openclassrooms.safetyNetAlerts.model.DataPerStation;
import dao.FirestationDAO;
import dao.MedicalRecordDAO;
import dao.PersonDAO;
import model.DataPerAddress;
import model.Firestation;
import model.Person;
import model.PersonInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataOrganizationService {

    private final PersonDAO personDAO = new PersonDAO();
    private final FirestationDAO firestationDAO = new FirestationDAO();
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();

    private DataPerAddress getByAddress(String address) {

        DataPerAddress dataPerAddress = new DataPerAddress();
        dataPerAddress.setFirestations(firestationDAO.getByAddress(address));
        dataPerAddress.setPersons(personDAO.getByAddress(address));

        return dataPerAddress;
    }

    private PersonInfo getByName(String firstName, String lastName) {

        PersonInfo personInfo = new PersonInfo();
        personInfo.setPerson(personDAO.getByName(firstName, lastName));
        personInfo.setMedicalRecord(medicalRecordDAO.getByName(firstName, lastName));

        return personInfo;
    }

    public DataPerStation getByStation(String station) {

        DataPerStation dataPerStation = new DataPerStation();

        List<String> addresses = firestationDAO.getByStation(station)
                .stream()
                .map(Firestation::getAddress)
                .collect(Collectors.toList());

        List<PersonInfo> personInfos = new ArrayList<>();

        for (String address : addresses) {
            for (Person person : this.getByAddress(address).getPersons()) {
                personInfos.add(this.getByName(person.getFirstName(), person.getLastName()));
            }
        }

        dataPerStation.setPersonInfos(personInfos);

        for (PersonInfo personInfo : personInfos) {
            if (GetAge.getAge(personInfo.getMedicalRecord().getBirthdate()) < 18) {
                dataPerStation.addAChild();
            } else {
                dataPerStation.addAnAdult();
            }
        }

        return dataPerStation;
    }


}

