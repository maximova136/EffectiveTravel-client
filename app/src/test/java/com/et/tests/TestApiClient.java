package com.et.tests;


import com.et.api.ApiClient;
import com.et.auth.Auth;
import com.et.exception.api.ApiCallException;
import com.et.response.StatisticsObject;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestApiClient {

    @BeforeClass
    public static void setUpSuite() {
        Auth.login("admin", "admin");
    }

    @Test
    public void fetchStats() {
        ApiClient client = ApiClient.instance();

        try {
            StatisticsObject resp = client.statistics(5, 15);
            Assert.assertTrue("No actual stats", resp.getFridayFreq().size() > 0);
            Assert.assertTrue("No actual stats", resp.getWeekdaysFreq().size() > 0);
            Assert.assertTrue("No actual stats", resp.getWeekendFreq().size() > 0);
        }
        catch (ApiCallException e) {
            e.printStackTrace();
            Assert.fail("No exceptions today");
        }
    }


    @Test
    public void submitNote() {
        ApiClient client = ApiClient.instance();
        Assert.assertTrue("Should succeed", client.submitNote(33, 33, "12:33"));

    }

}
