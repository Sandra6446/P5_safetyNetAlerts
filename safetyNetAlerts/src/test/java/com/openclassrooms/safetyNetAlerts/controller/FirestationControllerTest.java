package com.openclassrooms.safetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNetAlerts.dao.FirestationsDAO;
import com.openclassrooms.safetyNetAlerts.exceptions.AlreadyInDataFileException;
import com.openclassrooms.safetyNetAlerts.exceptions.NotFoundInDataFileException;
import com.openclassrooms.safetyNetAlerts.model.Firestation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FirestationController.class)
public class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FirestationsDAO firestationsDAO;

    private final Firestation firestationForTest = new Firestation();

    @BeforeEach
    private void setUp() {
        firestationForTest.setAddress("Ici");
        firestationForTest.setStation("1");
    }

    @Test
    public void add() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String firestationAsString = objectMapper.writeValueAsString(firestationForTest);

        // Test to save a Firestation
        when(firestationsDAO.save(any(Firestation.class))).thenReturn(firestationForTest);
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isCreated());

        // Test to save a Firestation already in data file
        when(firestationsDAO.save(any(Firestation.class))).thenThrow(AlreadyInDataFileException.class);
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isConflict());

        // Test to save an empty Firestation
        Firestation firestation = new Firestation();
        firestationAsString = objectMapper.writeValueAsString(firestation);
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void update() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        String firestationAsString = objectMapper.writeValueAsString(firestationForTest);

        // Test to update a Firestation
        when(firestationsDAO.update(any(Firestation.class))).thenReturn(firestationForTest);
        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isOk());

        // Test to update a Firestation not in data file
        when(firestationsDAO.update(any(Firestation.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isNotFound());

        // Test to update an empty Firestation
        Firestation firestation = new Firestation();
        firestationAsString = objectMapper.writeValueAsString(firestation);
        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void remove() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String firestationAsString = objectMapper.writeValueAsString(firestationForTest);

        // Test to remove a Firestation
        when(firestationsDAO.remove(any(Firestation.class))).thenReturn(true);
        mockMvc.perform(delete("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isOk());

        // Test to remove a Firestation not in data file
        when(firestationsDAO.remove(any(Firestation.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(delete("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isNotFound());
    }
}
