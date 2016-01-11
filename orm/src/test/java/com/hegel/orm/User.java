package com.hegel.orm;

import com.hegel.orm.annotations.AutoIncrement;
import com.hegel.orm.annotations.Id;

/**
 * CREATE TABLE User (
 *   id            INT AUTO_INCREMENT,
 *   name          VARCHAR(60),
 *   login         VARCHAR(20),
 *   password      VARCHAR(20),
 *   is_txt_enable BOOL,
 *
 *   PRIMARY KEY (id)
 * )
 */
public class User {

    @Id
    @AutoIncrement
    private int id;

    private String name;

    private String login;

    private String password;

    private boolean isTxtEnable;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isTxtEnable() {
        return isTxtEnable;
    }
}
