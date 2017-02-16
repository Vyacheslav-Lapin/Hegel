package com.hegel.orm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class JdbcDaoTest {

    private static final String RESOURCES_FILE_PATH = "src/test/resources/";

    private static ConnectionPool connectionPool = ConnectionPool.create(RESOURCES_FILE_PATH);

    @Test
    void insertWithStatement() throws SQLException {

        assertThat(
                connectionPool.executor(
                        "INSERT INTO Person (first_name, last_name) VALUES ('Jose', 'Eglesias')"
                ).getOrThrowUnchecked(),

                is(1));

        connectionPool.executor("DELETE FROM Person WHERE first_name='Jose' AND last_name='Eglesias'")
                .executeOrThrowUnchecked();
    }

    @Test
    void insertWithPreparedStatement() throws SQLException {
        assertThat(
                connectionPool.preparedExecutor(
                        "INSERT INTO Person (first_name, last_name) VALUES (?,?)")
                        .getOrThrowUnchecked("Jose", "Eglesias"),

                is(1)
        );

        connectionPool.executor("DELETE FROM Person WHERE first_name='Jose' AND last_name='Eglesias'")
                .executeOrThrowUnchecked();
    }

    @Test
    void bachInsertWithStatement() throws SQLException {
//        assertThat(
//                connectionPool.executor(
//                        "INSERT INTO Person (first_name, last_name) VALUES ('Anju', 'Eglesias')",
//                        "INSERT INTO Person (first_name, last_name) VALUES ('Sonia', 'Marmeladova')",
//                        "INSERT INTO Person (first_name, last_name) VALUES ('Asha', 'Eglesias')"
//                ).getOrThrowUnchecked(),
//
//                is(new int[]{1, 1, 1})
//        );
//
//        connectionPool.executor(
//                "DELETE FROM Person WHERE first_name='Anju' AND last_name='Eglesias'",
//                "DELETE FROM Person WHERE first_name='Sonia' AND last_name='Marmeladova'",
//                "DELETE FROM Person WHERE first_name='Asha' AND last_name='Eglesias'"
//        ).executeOrThrowUnchecked();
    }

    @Test
    void getObjects() {
//        assertThat(
//                connectionPool.executor(
//                        "INSERT INTO Person (first_name, last_name) VALUES ('Anju', 'Eglesias')",
//                        "INSERT INTO Person (first_name, last_name) VALUES ('Sonia', 'Marmeladova')",
//                        "INSERT INTO Person (first_name, last_name) VALUES ('Asha', 'Eglesias')"
//                ).getOrThrowUnchecked(),
//
//                is(new int[]{1, 1, 1}));
//
//        connectionPool.executor(
//                "DELETE FROM Person WHERE first_name='Anju' AND last_name='Eglesias'",
//                "DELETE FROM Person WHERE first_name='Sonia' AND last_name='Marmeladova'",
//                "DELETE FROM Person WHERE first_name='Asha' AND last_name='Eglesias'"
//        ).executeOrThrowUnchecked();

//        assertThat(connectionPool.requestObjects(Person.class).collect(toSet()),
//                hasItems(new Person(1, "Anju"), new Person(2, "Sonia"), new Person(3, "Asha")));
    }

    private void printPersons() {
//        connectionPool.requestCollection(
//                rs -> new Person(rs.getInt("id"), rs.getString("name")),
//                "SELECT id, name FROM Person"
//        ).ifPresent(persons -> persons.forEach(System.out::println));
    }


    @AfterEach
    void dropTable() {
//        printPersons();
//        connectionPool.executor("DROP TABLE Person");
    }
}
