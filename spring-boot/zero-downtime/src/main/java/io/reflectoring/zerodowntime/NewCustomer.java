package io.reflectoring.zerodowntime;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("CUSTOMER")
public class NewCustomer {

    @Id
    private long id;
    private String firstName;
    private String lastName;
    private String addressStreet;
    private String addressStreetNumber;

    public NewCustomer(String firstName, String lastName, String addressStreet, String addressStreetNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressStreet = addressStreet;
        this.addressStreetNumber = addressStreetNumber;
    }

    @Override
    public String toString() {
        return "NewCustomer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", addressStreet='" + addressStreet + '\'' +
                ", addressStreetNumber='" + addressStreetNumber + '\'' +
                '}';
    }
}
