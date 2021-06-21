package com.openclassrooms.safetyNetAlerts.service;

import com.openclassrooms.safetyNetAlerts.model.PersonInfo;
import com.openclassrooms.safetyNetAlerts.dao.MedicalRecordDAO;
import com.openclassrooms.safetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.safetyNetAlerts.model.Person;

public class PersonInfoService {

    private static final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();

    public static class PersonInfoBuilder {

        private Person person;

        public PersonInfoBuilder withPerson(Person person) {
            this.person = person;
            return this;
        }

        public PersonInfo build() {

            PersonInfo personInfo = new PersonInfo();
            personInfo.setFirstName(person.getFirstName());
            personInfo.setLastName(person.getLastName());
            personInfo.setAddress(person.getAddress());
            personInfo.setCity(person.getCity());
            personInfo.setZip(person.getZip());
            personInfo.setPhone(person.getPhone());
            personInfo.setEmail(person.getEmail());

            MedicalRecord medicalRecord = medicalRecordDAO.getByName(person.getFirstName(), person.getLastName());
            personInfo.setMedicalRecord(medicalRecord);
            personInfo.setAge(GetAge.getAge(medicalRecord.getBirthdate()));

            return personInfo;
        }
    }
}
