package com.hegel.orm;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface ConnectionPool extends DbService {

    String DEFAULT_DB_PROPERTIES_FILE_PATH = "db.properties";

    static ConnectionPool make() {
        return from(DEFAULT_DB_PROPERTIES_FILE_PATH);
    }

    static ConnectionPool from(String propertiesFilePath) {
        return from(Paths.get(propertiesFilePath));
    }

    static ConnectionPool from(Path propertiesFilePath) {
        return null;
    }
}