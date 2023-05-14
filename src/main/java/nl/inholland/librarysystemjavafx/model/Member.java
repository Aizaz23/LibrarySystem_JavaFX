package nl.inholland.librarysystemjavafx.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    private int memberId;
    private int itemCode;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    public Member() {
    }

    public Member(int memberId, String firstName, String lastName, LocalDate birthDate) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getItemCode() {
        return itemCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

}
