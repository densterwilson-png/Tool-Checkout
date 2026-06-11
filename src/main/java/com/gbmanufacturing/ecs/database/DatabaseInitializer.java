package com.gbmanufacturing.ecs.database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String DB_DIR = "database";

    public static void init() throws Exception {
        ensureDataDirectoryExists();
        try (Connection connection = DatabaseManager.getConnection(); Statement statement = connection.createStatement()) {
            createSchema(statement);
            DatabaseSeeder.seedDatabase(connection);
        }
    }

    private static void ensureDataDirectoryExists() throws Exception {
        Path dir = Path.of(DB_DIR);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
    }

    private static void createSchema(Statement statement) throws Exception {
        statement.execute("CREATE TABLE IF NOT EXISTS tools (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, status TEXT NOT NULL, checkedOutBy TEXT, checkedOutAt TEXT)");
        statement.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE NOT NULL, password TEXT NOT NULL, first_name TEXT, last_name TEXT, role TEXT NOT NULL)");
        statement.execute("CREATE TABLE IF NOT EXISTS equipment (id INTEGER PRIMARY KEY AUTOINCREMENT, equipment_name TEXT NOT NULL, equipment_type TEXT NOT NULL, condition_status TEXT NOT NULL, availability_status TEXT NOT NULL, required_certification TEXT)");
        statement.execute("CREATE TABLE IF NOT EXISTS user_certifications (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, certification_type TEXT NOT NULL)");
    }
}
