package com.gbmanufacturing.ecs.database;

import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class DatabaseManagerTest {
    @Test
    public void getConnectionReturnsOpenConnection() throws Exception {
        try (Connection connection = DatabaseManager.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());
            assertNotNull(connection.getMetaData());
        }
    }
}
