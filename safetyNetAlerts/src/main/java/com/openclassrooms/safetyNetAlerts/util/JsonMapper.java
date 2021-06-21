package com.openclassrooms.safetyNetAlerts.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNetAlerts.model.ObjectFromJson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * This class read and write in Json data file.
 */
public class JsonMapper {

    /**
     * Data file path.
     */
    //@Value("${chemin_json}")
    private final String chemin = "C:/Users/Maysonnave/Documents/OpenClassrooms/safetynetalert/safetyNetAlerts/src/main/resources/data.json";

    private static final Logger logger = LogManager.getLogger(ObjectFromJson.class);

    /**
     * Read in data file
     * @return Returns an objectFromJson if the reading succeed.
     * @see ObjectFromJson
     */
    public ObjectFromJson readJson() {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectFromJson objectFromJson = new ObjectFromJson();

        try {
            //convert json string to object
            objectFromJson = objectMapper.readValue(new File(chemin), ObjectFromJson.class);
            logger.debug("Json file correctly read.");
        } catch (IOException e) {
            logger.error("Error while JSON file reading.");
        }

        return objectFromJson;
    }

    /**
     * Write in data file
     * @param objectFromJson The objectFromJson to be added in data file.
     * @see ObjectFromJson
     */
    public void writeJson(ObjectFromJson objectFromJson) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //convert Object to json string
            objectMapper.writeValue(new File(chemin), objectFromJson);
            logger.debug("Json file correctly written.");
        } catch (IOException e) {
            logger.error("Error while JSON file writing.");
        }

    }

}

