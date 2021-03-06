package API.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import API.model.JsonObject;
import API.util.UtilsForTest;
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
public class ChildAlertControllerIT {

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
        Logger logger = LogManager.getLogger(PersonControllerIT.class);
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
    public void getChildInfos() throws Exception {
            mockMvc.perform(get("/childAlert?address=1509 Culver St"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].children[0].firstName",is(jsonObject.getPersons().get(0).getFirstName()))) // First child in jsonObject
                    .andExpect(jsonPath("$[0].adults[0].firstName",is(jsonObject.getPersons().get(1).getFirstName()))); // First adult in jsonObject
    }
}