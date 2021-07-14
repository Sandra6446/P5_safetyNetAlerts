package API.controller;

import API.dao.MedicalRecordsDAO;
import API.exceptions.AlreadyInDataFileException;
import API.exceptions.NotFoundInDataFileException;
import API.model.MedicalRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public void add() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String medicalRecordAsString = objectMapper.writeValueAsString(medicalRecordForTest);

        // Test to save a MedicalRecord
        when(medicalRecordsDAO.save(any(MedicalRecord.class))).thenReturn(medicalRecordForTest);
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isCreated());

        // Test to save a MedicalRecord already in data file
        when(medicalRecordsDAO.save(any(MedicalRecord.class))).thenThrow(AlreadyInDataFileException.class);
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isConflict());

        // Test to save an empty MedicalRecord
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecordAsString = objectMapper.writeValueAsString(medicalRecord);
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void update() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String medicalRecordAsString = objectMapper.writeValueAsString(medicalRecordForTest);

        // Test to update a MedicalRecord
        when(medicalRecordsDAO.update(any(MedicalRecord.class))).thenReturn(medicalRecordForTest);
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isOk());

        // Test to update a MedicalRecord not in data file
        when(medicalRecordsDAO.update(any(MedicalRecord.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isNotFound());

        // Test to update an empty MedicalRecord
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecordAsString = objectMapper.writeValueAsString(medicalRecord);
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void remove() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String medicalRecordAsString = objectMapper.writeValueAsString(medicalRecordForTest);

        // Test to remove a MedicalRecord
        when(medicalRecordsDAO.remove(any(MedicalRecord.class))).thenReturn(true);
        mockMvc.perform(delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isOk());

        // Test to remove a MedicalRecord not in data file
        when(medicalRecordsDAO.remove(any(MedicalRecord.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(delete("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(medicalRecordAsString))
                .andExpect(status().isNotFound());
    }
}
