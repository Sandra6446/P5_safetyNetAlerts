package com.safetynetalerts.microservicefirestation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.FirestationDAO;
import dao.PersonDAO;
import exceptions.AlreadyInDataFileException;
import exceptions.NotFoundInDataFileException;
import model.Firestation;
import model.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import util.JsonMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FirestationController.class)
public class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JsonMapper jsonMapper;

    @MockBean
    private FirestationDAO firestationDAO;

    private static Firestation firestationForTest;

    @BeforeAll
    public static void setUp() {
        firestationForTest = new Firestation();
        firestationForTest.setAddress("Ici");
        firestationForTest.setStation("1");
    }

    @Test
    public void testadd() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String firestationAsString = objectMapper.writeValueAsString(firestationForTest);

        when(firestationDAO.save(any(Firestation.class))).thenReturn(firestationForTest);
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isCreated());

        when(firestationDAO.save(any(Firestation.class))).thenThrow(AlreadyInDataFileException.class);
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

        when(firestationDAO.update(any(Firestation.class))).thenReturn(firestationForTest);
        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isOk());

        when(firestationDAO.update(any(Firestation.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isNotFound());

        /*Firestation firestation = new Firestation();
        firestationAsString = objectMapper.writeValueAsString(firestation);
        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isBadRequest());*/
    }

    @Test
    public void testDelete() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String firestationAsString = objectMapper.writeValueAsString(firestationForTest);

        when(firestationDAO.remove(any(Firestation.class))).thenReturn(true);
        mockMvc.perform(delete("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isOk());

        when(firestationDAO.remove(any(Firestation.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(delete("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(firestationAsString))
                .andExpect(status().isNotFound());
    }

}
