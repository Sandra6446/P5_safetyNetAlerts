package com.safetynetalert.microservicemedicalRecord.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MedicalRecordDAO;
import exceptions.AlreadyInDataFileException;
import exceptions.NotFoundInDataFileException;
import model.MedicalRecord;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JsonMapper jsonMapper;

    @MockBean
    private MedicalRecordDAO medicalRecordDAO;

    private static MedicalRecord medicalRecordForTest;

    @BeforeAll
    public static void setUp() {
        medicalRecordForTest = new MedicalRecord();
        medicalRecordForTest.setFirstName("Sandra");
        medicalRecordForTest.setLastName("M");
        medicalRecordForTest.setBirthdate("01/01/1980");
    }

    @Test
    public void testadd() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String medicalRecordAsString = objectMapper.writeValueAsString(medicalRecordForTest);

        when(medicalRecordDAO.save(any(MedicalRecord.class))).thenReturn(medicalRecordForTest);
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isCreated());

        when(medicalRecordDAO.save(any(MedicalRecord.class))).thenThrow(AlreadyInDataFileException.class);
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isConflict());

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecordAsString = objectMapper.writeValueAsString(medicalRecord);
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String medicalRecordAsString = objectMapper.writeValueAsString(medicalRecordForTest);

        when(medicalRecordDAO.update(any(MedicalRecord.class))).thenReturn(medicalRecordForTest);
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isOk());

        when(medicalRecordDAO.update(any(MedicalRecord.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isNotFound());

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecordAsString = objectMapper.writeValueAsString(medicalRecord);
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String medicalRecordAsString = objectMapper.writeValueAsString(medicalRecordForTest);

        when(medicalRecordDAO.remove(any(MedicalRecord.class))).thenReturn(true);
        mockMvc.perform(delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isOk());

        when(medicalRecordDAO.remove(any(MedicalRecord.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isNotFound());
    }

}
