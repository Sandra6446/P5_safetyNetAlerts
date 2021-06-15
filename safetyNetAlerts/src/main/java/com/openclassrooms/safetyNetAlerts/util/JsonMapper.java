package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.ObjectFromJson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

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
    private String chemin = "../common-classes/src/main/resources/data.json";

    private static final Logger logger = LogManager.getLogger(ObjectFromJson.class);

    /**
     * Read in data file
     * @return Returns an objectFromJson if the reading succeed.
     * @see model.ObjectFromJson
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
     * @see model.ObjectFromJson
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

