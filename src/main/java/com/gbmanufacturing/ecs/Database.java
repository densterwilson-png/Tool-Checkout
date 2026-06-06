package com.gbmanufacturing.ecs;

import com.gbmanufacturing.ecs.database.DatabaseManager;
import java.sql.Connection;

public class Database {
    public static Connection getConnection() throws Exception {
        return DatabaseManager.getConnection();
    }
}

