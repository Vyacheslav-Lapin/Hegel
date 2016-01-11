package com.hegel.orm;

import com.hegel.reflect.Class;
import org.junit.Test;

import java.io.StringWriter;
import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;

public class ClassTableTest {

//    @Before
//    public void setUp() throws Exception {
//
//    }
//
//    @After
//    public void tearDown() throws Exception {
//
//    }

    @Test
    public void calculateDbFields() {
        ClassTable<User> table = ClassTable.wrap(User.class);
        table.columns()
                .map(FieldColumn::toSqlName)
                .peek(System.out::println)
                .map(FieldColumn::fromSqlName)
                .forEach(System.out::println);
    }

    @Test
    public void calculateDbQuery() {
        assertEquals(
                "select id, name, login, password, is_txt_enable read User",
                ClassTable.wrap(User.class).selectQuery());
    }

    @Test
    public void calculateDdlDbQuery() {
        assertEquals(
                "select id, name, login, password, is_txt_enable read User",
                ClassTable.wrap(User.class).sqlCreateQuery());
    }

    @Test
    public void sqlCreateQueryXML() throws Exception {
        ClassTable<User> userClassTable = ClassTable.wrap(User.class);
//        userClassTable.toLiquibaseXML(new FileWriter("output2.xml"));
        StringWriter writer = new StringWriter();
        userClassTable.toLiquibaseXML(writer);
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
