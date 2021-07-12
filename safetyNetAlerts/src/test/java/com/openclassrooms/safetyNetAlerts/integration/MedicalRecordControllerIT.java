package com.openclassrooms.safetyNetAlerts.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNetAlerts.model.JsonObject;
import com.openclassrooms.safetyNetAlerts.model.MedicalRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MedicalRecordControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Value("${jsonPath}")
    private String path;

    private static MedicalRecord medicalRecordBody;

    @BeforeAll
    private static void setUp() {
        medicalRecordBody = new MedicalRecord("John", "Boyd", "01/01/2016", new ArrayList<>(Arrays.asList("aznol:350mg", "hydrapermazol:100mg")), new ArrayList<>(Collections.singletonList("nillacilan")));
    }

    @BeforeEach
    private void setUpPerTest() {
        Logger logger = LogManager.getLogger(PersonControllerIT.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonObject jsonObject = new JsonObject();
        try {
            //convert Object to json string
            objectMapper.writeValue(new File(path), jsonObject);
            logger.debug("Json test file correctly written.");
        } catch (IOException e) {
            logger.error("Error while test JSON file writing.");
        }
    }

    @Test
    public void add() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String medicalRecordAsString = objectMapper.writeValueAsString(medicalRecordBody);

        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isCreated());
    }

    @Test
    public void update() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        medicalRecordBody.setBirthdate("01/01/2018");
        String medicalRecordAsString = objectMapper.writeValueAsString(medicalRecordBody);

        add();
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isOk());
    }

    @Test
    public void remove() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String medicalRecordAsString = objectMapper.writeValueAsString(medicalRecordBody);

        add();
        mockMvc.perform(delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isOk());
    }
}
