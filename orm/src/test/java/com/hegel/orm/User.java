package com.hegel.orm;

import com.hegel.orm.annotations.AutoIncrement;
import com.hegel.orm.annotations.Id;
import com.hegel.orm.annotations.Size;

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
class User {

    @Id
    @AutoIncrement
    private int id;

    @Size(60)
    private String name;

    @Size(20)
    private String login;

    @Size(20)
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
