package com.hegel.orm;

import org.junit.After;
import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DbServiceTest {

    private static final String RESOURCES_FILE_PATH = "src/test/resources/";
    private static final String DB_PROPERTIES_FILE_NAME = "db.properties";
    private static final String DB_PREPARE_FILE_NAME = "h2.sql";

    private DbService dbService = DbService.from(
            RESOURCES_FILE_PATH + DB_PROPERTIES_FILE_NAME,
            RESOURCES_FILE_PATH + DB_PREPARE_FILE_NAME);

    @Test
    public void insertWithStatement() throws SQLException {
        assertThat(
                dbService.execute("INSERT INTO Person (id, first_name, last_name) VALUES (1, 'Jose', 'Eglesias')"),
                is(1));
    }

    @Test
    public void insertWithPreparedStatement() throws SQLException {
        assertThat(
                dbService.execute(
                        "INSERT INTO Person (id, first_name, last_name) VALUES (?,?,?)",
                        1, "Jose", "Eglesias"),
                is(1));
    }

    @Test
    public void bachInsertWithStatement() throws SQLException {
        assertThat(dbService.execute(
                "INSERT INTO Person (id, first_name, last_name) VALUES (1, 'Anju', 'Eglesias')",
                "INSERT INTO Person (id, first_name, last_name) VALUES (2, 'Sonia', 'Marmeladova')",
                "INSERT INTO Person (id, first_name, last_name) VALUES (3, 'Asha', 'Eglesias')"
        ), is(new int[]{1, 1, 1}));
    }

    @Test
    public void getObjects() {
        assertThat(dbService.execute(
                "INSERT INTO Person (id, first_name, last_name) VALUES (1, 'Anju', 'Eglesias')",
                "INSERT INTO Person (id, first_name, last_name) VALUES (2, 'Sonia', 'Marmeladova')",
                "INSERT INTO Person (id, first_name, last_name) VALUES (3, 'Asha', 'Eglesias')"
        ), is(new int[]{1, 1, 1}));

//        assertThat(dbService.requestObjects(Person.class).collect(toSet()),
//                hasItems(new Person(1, "Anju"), new Person(2, "Sonia"), new Person(3, "Asha")));
    }

    private void printPersons() {
//        dbService.requestCollection(
//                rs -> new Person(rs.getInt("id"), rs.getString("name")),
//                "SELECT id, name FROM Person"
//        ).ifPresent(persons -> persons.forEach(System.out::println));
    }

    @After
    public void dropTable() {
//        printPersons();
        dbService.execute("DROP TABLE Person");
    }
}
