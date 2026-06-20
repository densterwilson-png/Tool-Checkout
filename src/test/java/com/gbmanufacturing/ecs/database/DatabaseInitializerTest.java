package com.gbmanufacturing.ecs.database;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.*;

public class DatabaseInitializerTest {
    @Test
    public void initCreatesDatabaseAndUsersTable() throws Exception {
        Path dbDir = Path.of("database");

        DatabaseInitializer.init();

        Path dbFile = dbDir.resolve("ecs.db");
        assertTrue(Files.exists(dbFile));

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='users'")) {
            assertTrue(resultSet.next());
            assertEquals("users", resultSet.getString("name"));
        }
    }
}
