package API.integration;

import API.model.JsonObject;
import API.util.UtilsForTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FloodControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Value("${jsonPath}")
    private String path;

    private static JsonObject jsonObject;

    @BeforeAll
    private static void setUp() {
        jsonObject = UtilsForTest.jsonObjectForIT();
    }

    @BeforeEach
    private void setUpPerTest() {
        Logger logger = LogManager.getLogger(FireControllerIT.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //convert Object to json string
            objectMapper.writeValue(new File(path), jsonObject);
            logger.debug("Json test file correctly written.");
        } catch (IOException e) {
            logger.error("Error while test JSON file writing.");
        }
    }

    @Test
    void getHouseHolds() throws Exception {

        // Check filter
        mockMvc.perform(get("/flood/stations?stations=1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].houses[0].street",is("1509 Culver St")))
                .andExpect(jsonPath("$[1].houses[0].street",is("29 15th St")))
                .andExpect(jsonPath("$[0].houses[0].residents[0].lastName",is("Boyd")))
                .andExpect(jsonPath("$[1].houses[0].residents[0].lastName",is("Marrack")));

    }
}