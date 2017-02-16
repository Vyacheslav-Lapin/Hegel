package model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * CREATE TABLE Person (
 *  id         INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
 *  first_name VARCHAR(60) NOT NULL,
 *  last_name  VARCHAR(40) NOT NULL,
 *  birth_date DATE,
 *
 *  UNIQUE (first_name, last_name)
 * );
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class Person {

    @NotNull
    private final long id;

    @NotNull
    @Size(max = 60)
    private final String firstName;

    @NotNull
    @Size(max = 40)
    private final String lastName;

    private final LocalDate birthDate;
}