package com.openclassrooms.safetyNetAlerts.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNetAlerts.model.Firestation;
import com.openclassrooms.safetyNetAlerts.model.JsonObject;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class FirestationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Value("${jsonPath}")
    private String path;

    private static Firestation firestationBody;

    @BeforeAll
    private static void setUp() {
        firestationBody = new Firestation("1509 Culver St", "1");
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
        String firestationAsString = objectMapper.writeValueAsString(firestationBody);

        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isCreated());
    }

    @Test
    public void update() throws Exception {
        add();
        ObjectMapper objectMapper = new ObjectMapper();
        firestationBody.setStation("2");
        String firestationAsString = objectMapper.writeValueAsString(firestationBody);

        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isOk());
    }

    @Test
    public void remove() throws Exception {
        add();
        ObjectMapper objectMapper = new ObjectMapper();
        String firestationAsString = objectMapper.writeValueAsString(firestationBody);

        mockMvc.perform(delete("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isOk());
    }
}
