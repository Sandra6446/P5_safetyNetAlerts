package API.controller;

import API.exceptions.BadRequestException;
import API.model.MyMap;
import API.model.MyPerson;
import API.service.CollectDataService;
import API.util.UtilsForTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FirestationRequestController.class)
public class FirestationRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FirestationRequestController firestationRequestController;

    @MockBean
    private CollectDataService collectDataService;

    private static List<MyMap> maps;

    @BeforeAll
    private static void setUp() {
        maps = UtilsForTest.myMapsForTest();
    } // A list of two Mymap, @see MyMapsForTest

    @Test
    public void getDataPerFirestation() throws Exception {

        when(collectDataService.buildMyMaps()).thenReturn(maps);

        LinkedHashMap<String, Object> myResident = new LinkedHashMap<>();
        MyPerson resident = maps.get(1).getHouses().get(0).getResidents().get(0);
        myResident.put("firstName", resident.getFirstName());
        myResident.put("lastName", resident.getLastName());
        myResident.put("phone", resident.getPhone());
        myResident.put("age",resident.getAge());

        // Check filter
            mockMvc.perform(get("/firestation?stationNumber=2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].houses[0].residents[0]", is(myResident)))
                    .andExpect(jsonPath("$[0].station").doesNotHaveJsonPath())
                    .andExpect(jsonPath("$[0].nbOfAdults",is(1)))
                    .andExpect(jsonPath("$[0].nbOfChildren",is(0)));

        // No station at this address
            mockMvc.perform(get("/firestation?stationNumber=nowhere"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());

        // Empty address
        assertThrows(BadRequestException.class, () -> firestationRequestController.getDataPerFirestation(""));
    }
}