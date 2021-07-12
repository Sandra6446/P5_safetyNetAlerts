package com.openclassrooms.safetyNetAlerts.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNetAlerts.model.Firestation;
import com.openclassrooms.safetyNetAlerts.model.JsonObject;
import com.openclassrooms.safetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.safetyNetAlerts.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PhoneAlertControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Value("${jsonPath}")
    private String path;

    private static JsonObject jsonObject;

    @BeforeAll
    private static void setUp() {
        jsonObject = new JsonObject();
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
        jsonObject.setMedicalrecords(new ArrayList<>(Arrays.asList(medicalRecord1,medicalRecord2,medicalRecord3)));
    }

    @BeforeEach
    private void setUpPerTest() {
        Logger logger = LogManager.getLogger(PersonControllerIT.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //convert Object to json string
            objectMapper.writeValue(new File(path), jsonObject);
            logger.debug("Json test file correctly written.");
        } catch (IOException e) {
            logger.error("Error while test JSON file writing.");
        }
    }

    @Test
    public void getPhoneNumbers() throws Exception {

        mockMvc.perform(get("/phoneAlert?firestation=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumbers[0]", is("841-874-6512")));
    }
}