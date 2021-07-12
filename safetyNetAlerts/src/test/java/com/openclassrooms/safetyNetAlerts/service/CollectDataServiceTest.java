package com.openclassrooms.safetyNetAlerts.service;

import com.openclassrooms.safetyNetAlerts.dao.FirestationsDAO;
import com.openclassrooms.safetyNetAlerts.dao.MedicalRecordsDAO;
import com.openclassrooms.safetyNetAlerts.dao.PersonsDAO;
import com.openclassrooms.safetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.safetyNetAlerts.model.Person;
import com.openclassrooms.safetyNetAlerts.util.UtilsForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CollectDataServiceTest {

    @MockBean
    private PersonsDAO personsDAO;

    @MockBean
    private MedicalRecordsDAO medicalRecordsDAO;

    private CollectDataService collectDataService;
    private static Person person;

    @BeforeAll
    private static void setUp() {
        person = new Person("John", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6512", "jaboyd@email.com");
    }

    @BeforeEach
    private void setUpPerTest() {
        when(personsDAO.getAll()).thenReturn(new ArrayList<>(Collections.singletonList(person)));
        MedicalRecord medicalRecord = new MedicalRecord("John", "Boyd","01/01/2016", new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")), new ArrayList<>(Collections.singletonList("nillacilan")));
        when(medicalRecordsDAO.getAll()).thenReturn(new ArrayList<>(Collections.singletonList(medicalRecord)));
        collectDataService = new CollectDataService(personsDAO,new FirestationsDAO(),medicalRecordsDAO);
    }

    @Test
    public void getAge() {
        String birthdate = "10/16/2018";
        Assertions.assertEquals(2, collectDataService.getAge(birthdate));
    }

    @Test
    public void convertPerson() {
        Assertions.assertEquals(UtilsForTest.myPersonsForTest().get(0), collectDataService.convertToAPersonne(person));
    }
}
