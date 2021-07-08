package com.openclassrooms.safetyNetAlerts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    public void testadd() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

            String firestationAsString = objectMapper.writeValueAsString(firestationForTest);

            when(firestationsDAO.save(any(Firestation.class))).thenReturn(firestationForTest);
            mockMvc.perform(post("/firestation")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(firestationAsString))
                    .andExpect(status().isCreated());

            when(firestationsDAO.save(any(Firestation.class))).thenThrow(AlreadyInDataFileException.class);
            mockMvc.perform(post("/firestation")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(firestationAsString))
                    .andExpect(status().isConflict());

            Firestation firestation = new Firestation();
            firestationAsString = objectMapper.writeValueAsString(firestation);
            mockMvc.perform(post("/firestation")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(firestationAsString))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

            String firestationAsString = objectMapper.writeValueAsString(firestationForTest);

            when(firestationsDAO.update(any(Firestation.class))).thenReturn(firestationForTest);
            mockMvc.perform(put("/firestation")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(firestationAsString))
                    .andExpect(status().isOk());

            when(firestationsDAO.update(any(Firestation.class))).thenThrow(NotFoundInDataFileException.class);
            mockMvc.perform(put("/firestation")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(firestationAsString))
                    .andExpect(status().isNotFound());

            Firestation firestation = new Firestation();
            firestationAsString = objectMapper.writeValueAsString(firestation);
            mockMvc.perform(put("/firestation")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(firestationAsString))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
            String firestationAsString = objectMapper.writeValueAsString(firestationForTest);

            when(firestationsDAO.remove(any(Firestation.class))).thenReturn(true);
            mockMvc.perform(delete("/firestation")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(firestationAsString))
                    .andExpect(status().isOk());

            when(firestationsDAO.remove(any(Firestation.class))).thenThrow(NotFoundInDataFileException.class);
            mockMvc.perform(delete("/firestation")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(firestationAsString))
                    .andExpect(status().isNotFound());
    }
}
