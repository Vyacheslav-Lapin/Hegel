package com.hegel.orm;

import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static com.hegel.core.test.Tests.TEST_RESOURCES_PATH;
import static lombok.AccessLevel.PRIVATE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@FieldDefaults(level = PRIVATE, makeFinal = true)
class JdbcDaoTest {

    static ConnectionPool connectionPool = ConnectionPool.create(TEST_RESOURCES_PATH);

    @Test
    void insertWithStatement() {

        assertThat(
                connectionPool.executor(
                        "insert into Person (first_name, last_name) values ('Jose', 'Eglesias')"
                ).getOrThrowUnchecked(),

                is(1));

        connectionPool.executor("delete from Person where first_name='Jose' and last_name='Eglesias'")
                .executeOrThrowUnchecked();
    }

    @Test
    void insertWithPreparedStatement() {

        assertThat(
                connectionPool.preparedExecutor(
                        "insert into Person (first_name, last_name) values (?,?)")
                        .getOrThrowUnchecked("Jose", "Eglesias"),

                is(1)
        );

        connectionPool.executor("delete from Person where first_name='Jose' and last_name='Eglesias'")
                .executeOrThrowUnchecked();
    }

    @Test
    void bachInsertWithStatement() {
//        assertThat(
//                connectionPool.executor(
//                        "insert into Person (first_name, last_name) values ('Anju', 'Eglesias')",
//                        "insert into Person (first_name, last_name) values ('Sonia', 'Marmeladova')",
//                        "insert into Person (first_name, last_name) values ('Asha', 'Eglesias')"
//                ).getOrThrowUnchecked(),
//
//                is(new int[]{1, 1, 1})
//        );
//
//        connectionPool.executor(
//                "delete from Person where first_name='Anju' and last_name='Eglesias'",
//                "delete from Person where first_name='Sonia' and last_name='Marmeladova'",
//                "delete from Person where first_name='Asha' and last_name='Eglesias'"
//        ).executeOrThrowUnchecked();
    }

    @Test
    void getObjects() {
//        assertThat(
//                connectionPool.executor(
//                        "insert into Person (first_name, last_name) values ('Anju', 'Eglesias')",
//                        "insert into Person (first_name, last_name) values ('Sonia', 'Marmeladova')",
//                        "insert into Person (first_name, last_name) values ('Asha', 'Eglesias')"
//                ).getOrThrowUnchecked(),
//
//                is(new int[]{1, 1, 1}));
//
//        connectionPool.executor(
//                "delete from Person where first_name='Anju' and last_name='Eglesias'",
//                "delete from Person where first_name='Sonia' and last_name='Marmeladova'",
//                "delete from Person where first_name='Asha' and last_name='Eglesias'"
//        ).executeOrThrowUnchecked();

//        assertThat(connectionPool.requestObjects(Person.class).collect(toSet()),
//                hasItems(new Person(1, "Anju"), new Person(2, "Sonia"), new Person(3, "Asha")));
    }

    private void printPersons() {
//        connectionPool.requestCollection(
//                rs -> new Person(rs.getInt("id"), rs.getString("name")),
//                "select id, name from Person"
//        ).ifPresent(persons -> persons.forEach(System.out::println));
    }


    @AfterEach
    void dropTable() {
//        printPersons();
//        connectionPool.executor("drop table Person");
    }
}
