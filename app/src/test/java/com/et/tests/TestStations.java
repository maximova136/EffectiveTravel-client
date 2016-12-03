package com.et.tests;

import com.et.auth.Auth;
import com.et.response.object.StationObject;
import com.et.stations.StationsList;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;



public class TestStations {
    private static Auth auth;
    private static StationsList stations;

    @BeforeClass
    public static void setUp() {
        auth = new Auth();
        stations = new StationsList();
        assertTrue(auth.login("admin", "admin"));
    }


    @Test
    public void testFetch() {
        assertTrue(stations.load());

        assertTrue(stations.getAll().size() >= 0);

        for (StationObject station : stations.getAll()) {
            assertTrue("Bad station title", station.getTitle().length() > 0);
            assertTrue("Bad s_id", station.getS_id() >= 0);
        }
    }
}
