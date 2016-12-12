package com.et.instrumented.tests;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.et.exception.storage.DeleteObjectFailed;
import com.et.response.StatisticsObject;
import com.et.response.object.FreqObject;
import com.et.stats.transport.TransportStatsCache;
import com.et.storage.AppSQliteDb;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestTransportStatsCache {

    private AppSQliteDb db;

    @Before
    public void setUpTest() {
        try {
            Context ctx = InstrumentationRegistry.getTargetContext();

            db = AppSQliteDb.init(ctx);

            db.open();

            db.clearCollection(TransportStatsCache.TABLE_NAME);
        }
        catch (DeleteObjectFailed e) {
            Assert.fail("Setup failed " + e.getMessage());
        }
    }

    @After
    public void tearDownTest() {
        try {
            if (db == null)
                return;

            db.clearCollection(TransportStatsCache.TABLE_NAME);

            db.close();

            db = null;
        } catch (DeleteObjectFailed e) {
            Assert.fail("Setup failed " + e.getMessage());
        }
    }


    @Test
    public void saveLoadCahedEntry() {
        TransportStatsCache cache = new TransportStatsCache(db);

        StatisticsObject entry = new StatisticsObject();

        ArrayList<FreqObject> f1 = new ArrayList<>();
        for(int i = 0; i < 20; i++) {

        }

        entry.setWeekdaysFreq(f1);
    }

}
