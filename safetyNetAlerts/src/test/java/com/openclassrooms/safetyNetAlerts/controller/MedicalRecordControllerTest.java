package com.openclassrooms.safetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNetAlerts.dao.MedicalRecordsDAO;
import com.openclassrooms.safetyNetAlerts.exceptions.AlreadyInDataFileException;
import com.openclassrooms.safetyNetAlerts.exceptions.NotFoundInDataFileException;
import com.openclassrooms.safetyNetAlerts.model.MedicalRecord;
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

@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordsDAO medicalRecordsDAO;

    private final MedicalRecord medicalRecordForTest = new MedicalRecord();

    @BeforeEach
    private void setUp() {
        medicalRecordForTest.setFirstName("Sandra");
        medicalRecordForTest.setLastName("M");
        medicalRecordForTest.setBirthdate("01/01/1980");
    }

    @Test
    public void testadd() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

            String medicalRecordAsString = objectMapper.writeValueAsString(medicalRecordForTest);

            when(medicalRecordsDAO.save(any(MedicalRecord.class))).thenReturn(medicalRecordForTest);
            mockMvc.perform(post("/medicalRecord")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(medicalRecordAsString))
                    .andExpect(status().isCreated());

            when(medicalRecordsDAO.save(any(MedicalRecord.class))).thenThrow(AlreadyInDataFileException.class);
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

            when(medicalRecordsDAO.update(any(MedicalRecord.class))).thenReturn(medicalRecordForTest);
            mockMvc.perform(put("/medicalRecord")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(medicalRecordAsString))
                    .andExpect(status().isOk());

            when(medicalRecordsDAO.update(any(MedicalRecord.class))).thenThrow(NotFoundInDataFileException.class);
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

            when(medicalRecordsDAO.remove(any(MedicalRecord.class))).thenReturn(true);
            mockMvc.perform(delete("/medicalRecord")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(medicalRecordAsString))
                    .andExpect(status().isOk());

            when(medicalRecordsDAO.remove(any(MedicalRecord.class))).thenThrow(NotFoundInDataFileException.class);
            mockMvc.perform(delete("/medicalRecord")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(medicalRecordAsString))
                    .andExpect(status().isNotFound());
    }
}
