package API.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the list of all the peaple living in the same place, with distinction between adults and children.
 *
 * @see House
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonFilter("familyFilter")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Family extends Address {

    /**
     * A list of all family's member under 18 years old
     */
    @JsonFilter("childrenFilter")
    private List<MyPerson> children;
    /**
     * A list of all family's member over 18 years old
     */
    @JsonFilter("adultsFilter")
    private List<MyPerson> adults;

    public Family(String address, String city, int zip) {
        super(address, city, zip);
        this.children = new ArrayList<>();
        this.adults = new ArrayList<>();
    }

    public Family(String address, String city, int zip, List<MyPerson> children, List<MyPerson> adults) {
        super(address, city, zip);
        this.children = children;
        this.adults = adults;
    }

    /**
     * Adds a MyPerson in the Family
     *
     * @param myPerson The MyPerson to be added
     */
    public void addFamilyMember(MyPerson myPerson) {
        if (myPerson.getAge() < 18) {
            this.children.add(myPerson);
        } else {
            this.adults.add(myPerson);
        }
    }

}
