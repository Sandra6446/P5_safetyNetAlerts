package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundInDataFileException extends RuntimeException {
    public NotFoundInDataFileException(String s) {
        super(s);
    }
}
