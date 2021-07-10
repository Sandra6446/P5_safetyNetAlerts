package com.openclassrooms.safetyNetAlerts.controller;

import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.model.MyMap;
import com.openclassrooms.safetyNetAlerts.service.CollectDataService;
import com.openclassrooms.safetyNetAlerts.service.DataServiceForTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FireController.class)
public class FireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FireController fireController;

    @MockBean
    private CollectDataService collectDataService;

    private static List<MyMap> maps;
    private static DataServiceForTest dataServiceForTest;

    @BeforeAll
    private static void setUp() {
        dataServiceForTest = new DataServiceForTest();
        maps = dataServiceForTest.buildMapsForTest();
    }

    @Test
    public void getDataPerAddress() throws Exception {

        when(collectDataService.buildMyMaps()).thenReturn(maps);

        LinkedHashMap<String, Object> myMedicalRecord = new LinkedHashMap<>();
        myMedicalRecord.put("medications", dataServiceForTest.getAdultMyMap2().getMyMedicalRecord().getMedications());
        myMedicalRecord.put("allergies", dataServiceForTest.getAdultMyMap2().getMyMedicalRecord().getAllergies());

        LinkedHashMap<String, Object> resident = new LinkedHashMap<>();
        resident.put("lastName", dataServiceForTest.getAdultMyMap2().getLastName());
        resident.put("phone", dataServiceForTest.getAdultMyMap2().getPhone());
        resident.put("age", dataServiceForTest.getAdultMyMap2().getAge());
        resident.put("myMedicalRecord", myMedicalRecord);

        // Check filter
            mockMvc.perform(get("/fire?address=" + dataServiceForTest.getAddressMyMap2().getStreet()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].houses[0].residents[0]").value(resident))
                    .andExpect(jsonPath("$[0].station").value("1"))
                    .andExpect(jsonPath("$[1].station").value("2"))
                    .andExpect(jsonPath("$[0].nbOfAdults").doesNotHaveJsonPath())
                    .andExpect(jsonPath("$[0].nbOfChildren").doesNotHaveJsonPath());

        // No station at this address;
            mockMvc.perform(get("/fire?address=nowhere"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());

        // Empty address
        assertThrows(BadRequestException.class, () -> fireController.getDataPerAddress(""));
    }
}
