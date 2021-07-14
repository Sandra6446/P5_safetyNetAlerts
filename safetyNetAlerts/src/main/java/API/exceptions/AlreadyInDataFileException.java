package API.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that can be thrown when the requested object is already in data file.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyInDataFileException extends RuntimeException {
    public AlreadyInDataFileException(String s) {
        super(s);
    }
}
