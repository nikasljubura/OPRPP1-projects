package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * Models a single student in database. Jmbag is unique.
 */
public class StudentRecord {

    private String jmbag;
    private String lastName;
    private String firstName;
    private int finalGrade;

    /**
     * Constructs a single studentrecord instance
     * @param jmbag
     * @param lastName
     * @param firstName
     * @param finalGrade
     */
    public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
        this.jmbag = jmbag;
        this.lastName = lastName;
        this.firstName = firstName;
        this.finalGrade = finalGrade;
    }

    public String getJmbag() {
        return jmbag;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getFinalGrade() {
        return finalGrade;
    }


    /**
     *
     * @param o instance of student record
     * @return true if students are equal, determined by jmbag
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentRecord)) return false;
        StudentRecord that = (StudentRecord) o;
        return Objects.equals(getJmbag(), that.getJmbag());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJmbag());
    }

}
