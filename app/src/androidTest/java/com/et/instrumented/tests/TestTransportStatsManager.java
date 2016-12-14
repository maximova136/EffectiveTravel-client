package com.et.instrumented.tests;


import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.et.api.ApiClient;
import com.et.auth.Auth;
import com.et.response.StatisticsObject;
import com.et.response.object.FreqObject;
import com.et.stats.personal.PersonalStatsManager;
import com.et.stats.transport.TransportStatsCache;
import com.et.stats.transport.TransportStatsManager;
import com.et.storage.AppSQliteDb;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestTransportStatsManager {

    private TransportStatsCache cache;

    public TestTransportStatsManager() {
        cache = new TransportStatsCache(AppSQliteDb.init(InstrumentationRegistry.getTargetContext()));
    }

    @BeforeClass
    public static void setUpSuite() {
        Auth.login("admin", "admin");
    }

    @AfterClass
    public static void tearDown() {
        AppSQliteDb.getInstance().close();
    }


    @Test
    public void fetchFromServer() {
        cache.clear();

        TransportStatsManager manager = new TransportStatsManager(ApiClient.instance(), AppSQliteDb.getInstance());

        StatisticsObject stats = manager.getStats(5, 15);

        Assert.assertTrue("No actual stats", stats.getFridayFreq().size() > 0);
        Assert.assertTrue("No actual stats", stats.getWeekdaysFreq().size() > 0);
        Assert.assertTrue("No actual stats", stats.getWeekendFreq().size() > 0);
    }

    @Test
    public void loadFromCache() {
        cache.clear();

        StatisticsObject stats = new StatisticsObject();

        ArrayList<FreqObject> f1 = new ArrayList<>();

        for(int i = 0; i < 45; i++) {
            String time = "" + i /2 + ":" + i;
            double freq = (float) i / 100;
            FreqObject freqObject = new FreqObject();
            freqObject.setTime(time);
            freqObject.setCount(freq);
            f1.add(freqObject);
        }

        stats.setWeekdaysFreq(f1);
        stats.setFridayFreq(f1);
        stats.setWeekendFreq(f1);

        cache.save(5, 15, stats);

        TransportStatsManager manager = new TransportStatsManager(ApiClient.instance(), AppSQliteDb.getInstance());

        StatisticsObject cachedStats = manager.getStats(5, 15);

        Assert.assertEquals("Objects are not equal", stats, cachedStats);
    }


    @Test
    public void submitNote() {
        TransportStatsManager manager = new TransportStatsManager(ApiClient.instance(), AppSQliteDb.getInstance());

        GregorianCalendar calendar = new GregorianCalendar();

        calendar.set(GregorianCalendar.MINUTE, calendar.get(GregorianCalendar.MINUTE) + 2);

        System.out.println(calendar.getTime().toString());

        Assert.assertTrue("Submit should not fail", manager.submitNote(495, 53, calendar.getTime()));
    }

}
