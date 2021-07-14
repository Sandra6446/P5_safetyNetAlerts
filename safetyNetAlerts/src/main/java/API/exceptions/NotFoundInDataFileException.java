package API.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that can be thrown when the requested object is not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundInDataFileException extends RuntimeException {
    public NotFoundInDataFileException(String s) {
        super(s);
    }
}
