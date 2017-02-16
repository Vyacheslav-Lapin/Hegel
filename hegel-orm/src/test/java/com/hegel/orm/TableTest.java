package com.hegel.orm;

import com.hegel.orm.columns.Column;
import com.hegel.properties.PropertyMap;
import com.hegel.reflect.Class;
import com.hegel.reflect.Reflect;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TableTest {

    private JdbcDao<?> dbService;

    @BeforeEach
    void setUp() throws Exception {
        Reflect.loadClass("org.h2.Driver");
        dbService = ConnectionPool.create(
                5,
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                PropertyMap.from("", "").toProperties());
    }

    @Test
    void calculateDbFields() {
        Table<User> table = Table.wrap(User.class);
        table.columns()
                .map(Column::toSqlName)
//                .peek(System.out::println)
                .map(Column::fromSqlName)
                .forEach(System.out::println);
    }

    @Test
    @Disabled
    void calculateSelectDbQuery() {
        assertThat(
                Table.wrap(User.class).selectQuery(),
                is("SELECT p.id, p.first_name, p.last_name, p.birth_date, u.email, u.password " +
                        "FROM User u, Person p"));
    }

    @Test
    @Disabled
    void calculateCreateDbQuery() {
        assertEquals(
                "CREATE TABLE User (" +
                        "    id            INT AUTO_INCREMENT," +
                        "    name          VARCHAR(60)," +
                        "    login         VARCHAR(20)," +
                        "    password      VARCHAR(20)," +
                        "    is_txt_enable BOOL," +
                        "    PRIMARY KEY (id)",
                Table.wrap(User.class).sqlCreateQuery());
    }

    @Test
    @Disabled
    void sqlCreateQueryXML() throws Exception {
        Table<User> userTable = Table.wrap(User.class);
//        userTable.toLiquibaseXML(new FileWriter("output2.xml"));
        StringWriter writer = new StringWriter();
        userTable.toLiquibaseXML(writer);
        System.out.println(writer.getBuffer().toString());
    }

    @Test
    @Disabled
    void getFromDB() throws ClassNotFoundException, SQLException {
        Class<User> userClass = Class.wrap(User.class);
//        userClass.fromDB()
    }
}
