package API.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


/**
 * This class represents a list of phone numbers and email address.
 */
@Data
@JsonFilter("contactFilter")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Contact {

    /**
     * A set of phone numbers to send text messages
     */
    private Set<String> phoneNumbers;

    /**
     * A set of emails to send messages
     */
    private Set<String> emails;

    public Contact() {
        this.phoneNumbers = new HashSet<>();
        this.emails = new HashSet<>();
    }

    /**
     * Adds a phone number to the contact
     *
     * @param phoneNumber The phone number to be added
     */
    public void addPhone(String phoneNumber) {
        this.phoneNumbers.add(phoneNumber);
    }

    /**
     * Adds an email to the contact
     *
     * @param email The email to be added
     */
    public void addEmail(String email) {
        this.emails.add(email);
    }
}
