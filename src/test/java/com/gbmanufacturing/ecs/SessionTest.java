package com.gbmanufacturing.ecs;

import org.junit.After;
import org.junit.Test;

import com.gbmanufacturing.ecs.auth.Session;
import com.gbmanufacturing.ecs.model.User;

import static org.junit.Assert.*;

public class SessionTest {
    @After
    public void tearDown() {
        Session.currentUser = null;
    }

    @Test
    public void currentUserCanBeSetAndRetrieved() {
        User user = new User();

        Session.currentUser = user;

        assertSame(user, Session.currentUser);
    }

    @Test
    public void defaultCurrentUserIsNull() {
        assertNull(Session.currentUser);
    }
}
