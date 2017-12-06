package model;

import com.hegel.orm.annotations.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

/**
 * CREATE TABLE User (
 * id            INT PRIMARY KEY,
 * email         VARCHAR(20),
 * password      VARCHAR(20),
 * <p>
 * CONSTRAINT user_from_person_inheritance FOREIGN KEY (id) REFERENCES Person(id)
 * );
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class User extends Person {

    @Size(20)
    String email;

    @Size(20)
    String password;

    public User(long id, String firstName, String lastName, LocalDate birthDate, String email, String password) {
        super(id, firstName, lastName, birthDate);
        this.email = email;
        this.password = password;
    }
}
