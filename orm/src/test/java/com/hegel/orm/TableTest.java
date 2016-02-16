package com.hegel.orm;

import com.hegel.reflect.Class;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.StringWriter;
import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;

public class TableTest {

    DbService dbService;

    @Before
    public void setUp() throws Exception {
        dbService = new H2DbService();
    }
//
//    @After
//    public void tearDown() throws Exception {
//
//    }

    @Test
    public void calculateDbFields() {
        Table<User> table = Table.wrap(User.class);
        table.columns()
                .map(Column::toSqlName)
                .peek(System.out::println)
                .map(Column::fromSqlName)
                .forEach(System.out::println);
    }

    @Test
    public void calculateSelectDbQuery() {
        assertEquals(
                "SELECT id, name, login, password, is_txt_enable FROM User",
                Table.wrap(User.class).selectQuery());
    }

    @Test
    @Ignore
    public void calculateCreateDbQuery() {

        

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
    public void sqlCreateQueryXML() throws Exception {
        Table<User> userTable = Table.wrap(User.class);
//        userTable.toLiquibaseXML(new FileWriter("output2.xml"));
        StringWriter writer = new StringWriter();
        userTable.toLiquibaseXML(writer);
        System.out.println(writer.getBuffer().toString());
    }

    @Test
    public void getFromDB() throws ClassNotFoundException, SQLException {
        Class<User> userClass = Class.wrap(User.class);

        java.lang.Class.forName("org.h2.Driver");
//        try (Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "", "")) {
//
//        }

//        userClass.fromDB()
    }
}
