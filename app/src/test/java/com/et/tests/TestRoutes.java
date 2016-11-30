package com.et.tests;

import com.et.api.Auth;
import com.et.api.Routes;
import com.et.exception.FetchException;
import com.et.responses.RouteObject;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestRoutes {
    private static Auth auth;
    private static Routes routes;

    @BeforeClass
    public static void setUp() {
        auth = new Auth();
        routes = new Routes();
        assertTrue(auth.login("admin", "admin"));
    }


    @Test
    public void testFetch() {
        try {
            routes.load();

            assertTrue(routes.getRoutes().size() >= 0);

            for (RouteObject route : routes.getRoutes()) {
                assertTrue(route.getTitle().length() > 0);
                assertTrue(route.getTransport_type().length() > 0);
                assertTrue(route.getR_id() > 0);
                assertTrue(route.getCost() >= 0);
            }

        }
        catch (FetchException e) {
            Assert.fail("Failed to fetch routes. Message: " + e.getMessage());
        }
    }
}