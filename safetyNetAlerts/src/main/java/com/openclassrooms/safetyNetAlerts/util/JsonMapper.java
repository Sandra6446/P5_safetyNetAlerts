package com.openclassrooms.safetyNetAlerts.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNetAlerts.model.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Reads and writes in Json data file.
 */
@Component
public class JsonMapper {

    /**
     * Data file path.
     */
    @Value("${cheminJson}")
    private String chemin = "./src/main/resources/data.json";

    private static final Logger logger = LogManager.getLogger(JsonObject.class);

    /**
     * Reads in json data file
     *
     * @return Returns an objectFromJson if the reading succeed.
     */
    public JsonObject readJson() {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonObject jsonObject = new JsonObject();
        System.out.println (chemin);

        try {
            //convert json string to object
            jsonObject = objectMapper.readValue(new File(chemin), JsonObject.class);
            logger.debug("Json file correctly read.");
        } catch (IOException e) {
            logger.error("Error while JSON file reading.");
        }

        return jsonObject;
    }

    /**
     * Writes in data file
     *
     * @param jsonObject The objectFromJson to be added in data file.
     */
    public void writeJson(JsonObject jsonObject) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //convert Object to json string
            objectMapper.writeValue(new File(chemin), jsonObject);
            logger.debug("Json file correctly written.");
        } catch (IOException e) {
            logger.error("Error while JSON file writing.");
        }

    }

}

