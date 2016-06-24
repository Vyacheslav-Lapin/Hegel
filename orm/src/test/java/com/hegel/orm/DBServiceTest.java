package com.hegel.orm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class DbServiceTest {

    private DbService dbService;

    @Before
    public void prepare() {
        assertThat((dbService = DbService.create("org.h2.Driver", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "", ""))
                .execute("CREATE TABLE Person (id INT PRIMARY KEY, name VARCHAR(255))"), is(0));
    }

    @Test
    public void insertWithStatement() throws SQLException {
        assertThat(dbService.execute("INSERT INTO Person (id, name) VALUES (1,'Jose')"), is(1));
    }

    @Test
    public void insertWithPreparedStatement() throws SQLException {
        assertThat(dbService.execute("INSERT INTO Person (id, name) VALUES (?,?)", 1, "Jose"), is(1));
    }

    @Test
    public void bachInsertWithStatement() throws SQLException {
        assertThat(dbService.execute(
                "INSERT INTO Person (id, name) VALUES (1, 'Anju')",
                "INSERT INTO Person (id, name) VALUES (2, 'Sonia')",
                "INSERT INTO Person (id, name) VALUES (3, 'Asha')"
        ), is(new int[]{1, 1, 1}));
    }

    @Test
    public void getObjects() {
        assertThat(dbService.execute(
                "INSERT INTO Person (id, name) VALUES (1, 'Anju')",
                "INSERT INTO Person (id, name) VALUES (2, 'Sonia')",
                "INSERT INTO Person (id, name) VALUES (3, 'Asha')"
        ), is(new int[]{1, 1, 1}));

        assertThat(dbService.requestObjects(Person.class).collect(toSet()),
                hasItems(new Person(1, "Anju"), new Person(2, "Sonia"), new Person(3, "Asha")));
    }

    private void printPersons() {
        dbService.requestCollection(
                rs -> new Person(rs.getInt("id"), rs.getString("name")),
                "SELECT id, name FROM Person"
        ).ifPresent(persons -> persons.stream().forEach(System.out::println));
    }

    @After
    public void dropTable() {
//        printPersons();
        dbService.execute("DROP TABLE Person");
    }
}
