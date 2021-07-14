package API.controller;

import API.exceptions.BadRequestException;
import API.model.MyMap;
import API.service.CollectDataService;
import API.util.UtilsForTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FloodController.class)
class FloodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FloodController floodController;

    @MockBean
    private CollectDataService collectDataService;

    private static List<MyMap> maps;

    @BeforeAll
    private static void setUp() {
        // A list of two MyMap, @see MyMapsForTest
        maps = UtilsForTest.myMapsForTest();
    }

    @Test
    void getHouseHolds() throws Exception {

        when(collectDataService.buildMyMaps()).thenReturn(maps);

        // Check filter
        mockMvc.perform(get("/flood/stations?stations=1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].houses[0]").exists())
                .andExpect(jsonPath("$[1].houses[0]").exists())
                .andExpect(jsonPath("$[0].houses[0].street").hasJsonPath())
                .andExpect(jsonPath("$[0].houses[0].city").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[0].houses[0].zip").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[0].houses[0].residents[0].firstName").doesNotHaveJsonPath())
                .andExpect(jsonPath("$[0].houses[0].residents[0].email").doesNotHaveJsonPath());

        // No coverage for these stations
        mockMvc.perform(get("/flood/stations?stations=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        // Empty station
        assertThrows(BadRequestException.class, () -> floodController.getHouseHolds(new HashSet<>()));

    }
}