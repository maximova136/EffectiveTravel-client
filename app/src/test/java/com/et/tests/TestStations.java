package com.et.tests;

import com.et.api.Auth;
import com.et.api.Routes;
import com.et.api.Stations;
import com.et.exception.BadResponseException;
import com.et.exception.FetchException;
import com.et.responses.BaseResponse;
import com.et.responses.RouteObject;
import com.et.responses.StationObject;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;



public class TestStations {
    private static Auth auth;
    private static Stations stations;

    @BeforeClass
    public static void setUp() {
        auth = new Auth();
        stations = new Stations();
        assertTrue(auth.login("admin", "admin"));
    }


    @Test
    public void testFetch() {
        try {
            stations.load();

            assertTrue(stations.getStations().size() >= 0);

            for (StationObject station : stations.getStations()) {
                assertTrue("Bad station title", station.getTitle().length() > 0);
                assertTrue("Bad s_id", station.getS_id() >= 0);
            }

        }
        catch (FetchException e) {
            Assert.fail("Failed to fetch stations. Message: " + e.getMessage());
        }
        catch (BadResponseException e) {
            Assert.fail("Failed to fetch stations. Success = false");
        }
    }
}
