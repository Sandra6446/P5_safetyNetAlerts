package API.controller;

import API.dao.PersonsDAO;
import API.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import API.exceptions.AlreadyInDataFileException;
import API.exceptions.NotFoundInDataFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonsDAO personsDAO;

    private Person personForTest;

    @BeforeEach
    private void setUpPerTest() {
        personForTest = new Person("John", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6513", "jaboyd@email.com");
    }

    @Test
    public void add() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personForTest);

        // Test to save a Person
        Mockito.when(personsDAO.save(ArgumentMatchers.any(Person.class))).thenReturn(personForTest);
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Test to save a Person already in data file
        Mockito.when(personsDAO.save(ArgumentMatchers.any(Person.class))).thenThrow(AlreadyInDataFileException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        // Test to save an empty Person
        Person person = new Person();
        personAsString = objectMapper.writeValueAsString(person);
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void update() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personForTest);

        // Test to update a Person
        Mockito.when(personsDAO.update(ArgumentMatchers.any(Person.class))).thenReturn(personForTest);
        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Test to update a Person not in data file
        Mockito.when(personsDAO.update(ArgumentMatchers.any(Person.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Test to update an empty Person
        Person person = new Person();
        personAsString = objectMapper.writeValueAsString(person);
        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void remove() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personForTest);

        // Test to remove a Person
        Mockito.when(personsDAO.remove(ArgumentMatchers.any(Person.class))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Test to remove a Person not in data file
        Mockito.when(personsDAO.remove(ArgumentMatchers.any(Person.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
