package model;

import com.hegel.orm.annotations.Size;
import lombok.*;

import java.time.LocalDate;

/**
 * CREATE TABLE User (
 *  id            INT PRIMARY KEY,
 *  email         VARCHAR(20),
 *  password      VARCHAR(20),
 *
 *  CONSTRAINT user_from_person_inheritance FOREIGN KEY (id) REFERENCES Person(id)
 * );
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class User extends Person {

    @Size(20)
    private String email;

    @Size(20)
    private String password;

    public User(long id, String firstName, String lastName, LocalDate birthDate, String email, String password) {
        super(id, firstName, lastName, birthDate);
        this.email = email;
        this.password = password;
    }
}
