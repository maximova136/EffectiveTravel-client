package com.et.instrumented.tests;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.response.object.StationObject;
import com.et.routes.RouteStorage;
import com.et.stations.StationsStorage;
import com.et.storage.AppSQliteDb;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestSQLiteDb {

    private AppSQliteDb db;

    public TestSQLiteDb() {  }

    @Before
    public void setUpTest() {
        try {
            Context ctx = InstrumentationRegistry.getTargetContext();
            db = new AppSQliteDb(ctx);

            db.clearCollection(RouteStorage.COLLECTION_NAME);
            db.clearCollection(StationsStorage.COLLECTION_NAME);
        }
        catch (DeleteObjectFailed e) {
            Assert.fail("Setup failed " + e.getMessage());
        }
    }

    @After
    public void tearDownTest() {
        try {
            if(db == null)
                return;

            db.clearCollection(RouteStorage.COLLECTION_NAME);
            db.clearCollection(StationsStorage.COLLECTION_NAME);

            db.close();
            db = null;
        }
        catch (DeleteObjectFailed e) {
            Assert.fail("Setup failed " + e.getMessage());
        }
    }

    @Test
    public void putLoadRoute() {
        HashMap<String, String> item = new HashMap<>();
        item.put(RouteStorage.TITLE_KEY, "test_tile");
        item.put(RouteStorage.R_ID_KEY,  "test_r_id");
        item.put(RouteStorage.COST_KEY,  "test_cost");
        item.put(RouteStorage.TYPE_KEY,  "test_type");

        try {
            db.putObject(RouteStorage.COLLECTION_NAME, item);
            List< HashMap<String, String> > collection = db.loadCollection(RouteStorage.COLLECTION_NAME);

            Assert.assertEquals("More than one object in collection", 1, collection.size());
            HashMap<String, String> loadedItem = collection.get(0);

            Assert.assertEquals("Inserted and loaded object differ", item, loadedItem);
        }
        catch (PutObjectFailed e) {
            Assert.fail("Failed to add row: " + e.getMessage());
        }
        catch (LoadCollectionFailed e) {
            Assert.fail("Failed to load collection: " + e.getMessage());
        }
    }


    @Test
    public void putLoadStation() {
        HashMap<String, String> item = new HashMap<>();
        item.put(StationsStorage.TITLE_KEY, "test_tile");
        item.put(StationsStorage.S_ID_KEY,  "test_s_id");

        try {
            db.putObject(StationsStorage.COLLECTION_NAME, item);
            List< HashMap<String, String> > collection = db.loadCollection(StationsStorage.COLLECTION_NAME);

            Assert.assertEquals("More than one object in collection", 1, collection.size());
            HashMap<String, String> loadedItem = collection.get(0);

            Assert.assertEquals("Inserted and loaded object differ", item, loadedItem);
        }
        catch (PutObjectFailed e) {
            Assert.fail("Failed to add row: " + e.getMessage());
        }
        catch (LoadCollectionFailed e) {
            Assert.fail("Failed to load collection: " + e.getMessage());
        }
    }
}
