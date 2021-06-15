package com.openclassrooms.safetyNetAlerts.service;

import com.openclassrooms.safetyNetAlerts.model.Adult;
import dao.MedicalRecordDAO;
import dao.PersonDAO;
import model.MedicalRecord;
import model.Person;

public class AdultService {

    private static PersonDAO personDAO = new PersonDAO();
    private static MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();

    public static class AdultBuilder {

        Person person;
        MedicalRecord medicalRecord;

        public AdultBuilder withFullName(Person person, MedicalRecord medicalRecord) {
            this.person = person;
            this.medicalRecord = medicalRecord;
            return this;
        }

        public Adult build() {
            Adult adult = new Adult();
            adult.setFirstName(person.getFirstName());
            adult.setLastName(person.getLastName());
            adult.setAddress(person.getAddress());
            adult.setCity(person.getCity());
            adult.setZip(person.getZip());
            adult.setPhone(person.getPhone());
            adult.setEmail(person.getEmail());
            adult.setMedicalRecord(medicalRecord);
            return adult;
        }
    }
}
