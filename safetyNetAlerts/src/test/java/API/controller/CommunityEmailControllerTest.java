package API.controller;

import API.exceptions.BadRequestException;
import API.service.CollectDataService;
import API.util.UtilsForTest;
import API.model.House;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommunityEmailController.class)
public class CommunityEmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommunityEmailController communityEmailController;

    @MockBean
    private CollectDataService collectDataService;

    private static List<House> houses;

    @BeforeAll
    private static void setUp() {
        // A list of two House in the same city, @see housesForTest
        houses = UtilsForTest.housesForTest();
    }

    @Test
    public void getEmails() throws Exception {

        when(collectDataService.buildHouses()).thenReturn(houses);

        // Contact filter check, no duplication
        mockMvc.perform(get("/communityEmail?city=" + houses.get(0).getCity()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.emails[0]",is("drk@email.com")))
                    .andExpect(jsonPath("$.phoneNumbers").doesNotHaveJsonPath());

        // No house in this city
            mockMvc.perform(get("/communityEmail?city=nowhere"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());

        // Empty city
        assertThrows(BadRequestException.class, () -> communityEmailController.getEmails(""));
    }

}