package com.gbmanufacturing.ecs;

import org.junit.Test;

import com.gbmanufacturing.ecs.model.User;

import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void userPropertiesAreAccessible() {
        User user = new User();

        assertEquals(12, user.getUserId());
        assertEquals("jsmith", user.getUsername());
        assertEquals("employee", user.getRole());
        assertEquals("John Smith", user.getFirstName());
        assertFalse(user.isSupervisor());
    }

    @Test
    public void isSupervisorIgnoresCase() {
        User user = new User();

        assertTrue(user.isSupervisor());
    }
}
