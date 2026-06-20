package com.gbmanufacturing.ecs;

import org.junit.Test;

import com.gbmanufacturing.ecs.model.User;

import static org.junit.Assert.*;

public class UserTest {
    @Test
public void userPropertiesAreAccessible() {
    User user = new User();

    user.setUserId(12);
    user.setUsername("jsmith");
    user.setRole(User.Role.EMPLOYEE);
    user.setFirstName("John Smith");

    assertEquals(12, user.getUserId());
    assertEquals("jsmith", user.getUsername());
    assertEquals(User.Role.EMPLOYEE, user.getRole());
    assertEquals("John Smith", user.getFirstName());
    assertFalse(user.isSupervisor());
}

    @Test
    public void isSupervisorIgnoresCase() {
        User user = new User();
        
        user.setRole(User.Role.SUPERVISOR);

        assertTrue(user.isSupervisor());
    }
}
