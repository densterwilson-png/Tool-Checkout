package com.gbmanufacturing.ecs.database;

import java.sql.Connection;

public class Database {
    public static Connection getConnection() throws Exception {
        return DatabaseManager.getConnection();
    }
}

