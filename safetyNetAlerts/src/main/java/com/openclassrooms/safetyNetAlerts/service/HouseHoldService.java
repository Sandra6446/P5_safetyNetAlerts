package com.openclassrooms.safetyNetAlerts.service;

import com.openclassrooms.safetyNetAlerts.model.Adult;
import com.openclassrooms.safetyNetAlerts.model.Child;
import com.openclassrooms.safetyNetAlerts.model.PersonInfo;
import com.openclassrooms.safetyNetAlerts.dao.MedicalRecordDAO;
import com.openclassrooms.safetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.safetyNetAlerts.model.Person;

public class PersonInfoService {

    private static final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();

    public static class PersonInfoBuilder {

        private Person person;

        public PersonInfoBuilder withFullName(Person person) {
            this.person = person;
            return this;
        }

        public PersonInfo build() {
            Adult adult = new Adult();
            Child child = new Child();
            MedicalRecord medicalRecord = medicalRecordDAO.getByName(person.getFirstName(), person.getLastName());
            int age = GetAge.getAge(medicalRecord.getBirthdate());
            if (age < 18) {
                child.setFirstName(person.getFirstName());
                child.setLastName(person.getLastName());
                child.setAddress(person.getAddress());
                child.setCity(person.getCity());
                child.setZip(person.getZip());
                child.setPhone(person.getPhone());
                child.setEmail(person.getEmail());
                child.setMedicalRecord(medicalRecord);
                child.setAge(age);
                return child;
            } else {
                adult.setFirstName(person.getFirstName());
                adult.setLastName(person.getLastName());
                adult.setAddress(person.getAddress());
                adult.setCity(person.getCity());
                adult.setZip(person.getZip());
                adult.setPhone(person.getPhone());
                adult.setEmail(person.getEmail());
                adult.setMedicalRecord(medicalRecord);
                adult.setAge(age);
                return adult;
            }

        }
    }
}
