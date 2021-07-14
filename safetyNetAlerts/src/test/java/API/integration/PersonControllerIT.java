package API.integration;

import API.model.JsonObject;
import API.model.Person;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PersonControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Value("${jsonPath}")
    private String path;

    private static Person personBody;

    @BeforeAll
    private static void setUp() {
        personBody = new Person("John", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6513", "jaboyd@email.com");
    }

    @BeforeEach
    private void setUpPerTest() {
        Logger logger = LogManager.getLogger(PersonControllerIT.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonObject jsonObject = new JsonObject();
        try {
            //convert Object to json string
            objectMapper.writeValue(new File(path), jsonObject);
            logger.debug("Json test file correctly written.");
        } catch (IOException e) {
            logger.error("Error while test JSON file writing.");
        }
    }

    @Test
    public void add() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personBody);
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void update() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personBody);

        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        add();

        Person person = personBody;
        person.setAddress("new street");
        personAsString = objectMapper.writeValueAsString(person);

        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void remove() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personBody);

        mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        add();

        mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
