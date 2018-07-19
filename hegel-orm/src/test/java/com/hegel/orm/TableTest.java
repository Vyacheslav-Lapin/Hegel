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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TableTest {

    private JdbcDao<?> dbService;

    @BeforeEach
    void setUp() {
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
                is("select p.id, p.first_name, p.last_name, p.birth_date, u.email, u.password " +
                        "from User u, Person p"));
    }

    @Test
    @Disabled
    void calculateCreateDbQuery() {
        assertEquals(
                "create table User (" +
                        "    id            identity," +
                        "    name          varchar(60)," +
                        "    login         varchar(20)," +
                        "    password      varchar(20)," +
                        "    is_txt_enable bool," +
                        "    primary key (id)" +
                        ")",
                Table.wrap(User.class).sqlCreateQuery());
    }

    @Test
    @Disabled
    void sqlCreateQueryXML() {
        Table<User> userTable = Table.wrap(User.class);
//        userTable.toLiquibaseXML(new FileWriter("output2.xml"));
        StringWriter writer = new StringWriter();
        userTable.toLiquibaseXML(writer);
        System.out.println(writer.getBuffer().toString());
    }

    @Test
    @Disabled
    void getFromDB() {
        Class<User> userClass = Class.wrap(User.class);
//        userClass.fromDB()
    }
}
