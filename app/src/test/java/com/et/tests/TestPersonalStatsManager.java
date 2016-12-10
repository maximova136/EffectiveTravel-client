package com.et.tests;


import com.et.exception.manager.InitPersonalStatsFailed;
import com.et.exception.manager.WrongTransportType;
import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.exception.storage.UpdateObjectFailed;
import com.et.routes.RouteType;
import com.et.stats.personal.IPersonalStatsStorage;
import com.et.stats.personal.PersonalStatsManager;
import com.et.stats.personal.PersonalStatsStorage;
import com.et.storage.ILocalStorage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPersonalStatsManager {
    private class MockLocalStorage implements ILocalStorage {
        public ArrayList< HashMap<String, Integer> > personalStatsList;

        public boolean clearDbCalled = false;

        public boolean failPut = false;
        public  boolean failDelete = false;
        public  boolean failLoad = false;

        public MockLocalStorage() {
            personalStatsList = new ArrayList<>();
        }

        public void clear() {
            personalStatsList.clear();
            clearDbCalled = false;
            failDelete = false;
            failPut = false;
            failLoad = false;
        }

        public void put(HashMap<String, Integer> item) {
            personalStatsList.add(item);
        }

        @Override
        public void putObject(String collection, HashMap<String, String> values) throws PutObjectFailed {
            if(failPut)
                throw new PutObjectFailed("Fail putObject for testing purposes");

            if(collection != PersonalStatsStorage.COLLECTION_NAME)
                throw new PutObjectFailed("Incorrect collection name");

            HashMap<String, Integer> item = new HashMap<>();
            for(Map.Entry<String, String> pair : values.entrySet() ) {
                item.put(pair.getKey(), Integer.parseInt(pair.getValue()));
            }

            personalStatsList.add(item);
        }

        @Override
        public void updateObject(String collection, int objectId, HashMap<String, String> values) throws UpdateObjectFailed {
            throw new UpdateObjectFailed("Should not be called");
        }

        @Override
        public void deleteObject(String collection, int objectId) throws DeleteObjectFailed {
            throw new DeleteObjectFailed("Should not be called");
        }

        @Override
        public void clearCollection(String collection) throws DeleteObjectFailed {
            if(failDelete)
                throw new DeleteObjectFailed("Fail clearCollection for testing purposes");

            if(collection != PersonalStatsStorage.COLLECTION_NAME)
                throw new DeleteObjectFailed("Incorrect collection name");

            personalStatsList.clear();
            clearDbCalled = true;
        }

        @Override
        public List<HashMap<String, String>> loadCollection(String collection) throws LoadCollectionFailed {
            if(failLoad)
                throw new LoadCollectionFailed("Fail loadCollection for testing purposes");

            if(collection != PersonalStatsStorage.COLLECTION_NAME)
                throw new LoadCollectionFailed("Incorrect collection name");

            ArrayList< HashMap<String, String> > collectionList = new ArrayList<>();

            for (HashMap<String, Integer> row : personalStatsList ) {
                HashMap<String, String> item = new HashMap<>();
                for(Map.Entry<String, Integer> pair : row.entrySet() ) {
                    item.put(pair.getKey(), pair.getValue().toString());
                }
                collectionList.add(item);
            }

            return collectionList;
        }
    }


    MockLocalStorage mockLocalStorage;

    public TestPersonalStatsManager() {
        mockLocalStorage = new MockLocalStorage();
    }

    @Before
    public void setUpTest() {
        mockLocalStorage.clear();
    }


    @Test
    public void loadEmpty() {
        PersonalStatsManager personalStatsManager = new PersonalStatsManager(mockLocalStorage);

        Assert.assertEquals("Stats should be empty", 0, personalStatsManager.getStats().size());

        try {
            int numberForType = personalStatsManager.getNumberOfTripsForType(RouteType.BUS);
            Assert.assertEquals("Stats for type should be 0", 0, numberForType);
            numberForType = personalStatsManager.getNumberOfTripsForType(RouteType.TRAM);
            Assert.assertEquals("Stats for type should be 0", 0, numberForType);
            numberForType = personalStatsManager.getNumberOfTripsForType(RouteType.TROLLEY);
            Assert.assertEquals("Stats for type should be 0", 0, numberForType);
            numberForType = personalStatsManager.getNumberOfTripsForType(RouteType.SHUTTLE);
            Assert.assertEquals("Stats for type should be 0", 0, numberForType);
        }
        catch (WrongTransportType e) {
            Assert.fail("Wrong transport type: " + e.getMessage());
        }
    }

    @Test
    public void initStats() {
        PersonalStatsManager personalStatsManager = new PersonalStatsManager(mockLocalStorage);

        HashMap<String, Integer> expectedStats = new HashMap<>();
        expectedStats.put(RouteType.BUS,     0);
        expectedStats.put(RouteType.TRAM,    0);
        expectedStats.put(RouteType.TROLLEY, 0);
        expectedStats.put(RouteType.SHUTTLE, 0);

        try {
            personalStatsManager.init();
            Assert.assertEquals("Manager should return correct stats rigth away", expectedStats, personalStatsManager.getStats());
            Assert.assertEquals("One item should be saved in storage", 1, mockLocalStorage.personalStatsList.size());
            Assert.assertEquals("Saved stats row should be correct", expectedStats, mockLocalStorage.personalStatsList.get(0));
        }
        catch (InitPersonalStatsFailed e) {
            Assert.fail("Init stats failed: " + e.getMessage());
        }
    }


    @Test
    public void initStatsFailing() {
        PersonalStatsManager personalStatsManager = new PersonalStatsManager(mockLocalStorage);

        mockLocalStorage.failPut = true;

        HashMap<String, Integer> expectedStats = new HashMap<>();
        expectedStats.put(RouteType.BUS,     0);
        expectedStats.put(RouteType.TRAM,    0);
        expectedStats.put(RouteType.TROLLEY, 0);
        expectedStats.put(RouteType.SHUTTLE, 0);

        try {
            personalStatsManager.init();
        }
        catch (InitPersonalStatsFailed e) {
            Assert.assertTrue(true);
            return;
        }
        Assert.fail("Init should have failed by now");
    }


    @Test
    public void getForType() {
        HashMap<String, Integer> expectedStats = new HashMap<>();
        expectedStats.put(RouteType.BUS,     4);
        expectedStats.put(RouteType.TRAM,    2);
        expectedStats.put(RouteType.TROLLEY, 1);
        expectedStats.put(RouteType.SHUTTLE, 5);
        mockLocalStorage.put(expectedStats);

        PersonalStatsManager personalStatsManager = new PersonalStatsManager(mockLocalStorage);

        Assert.assertEquals("Manager should return correct table", expectedStats, personalStatsManager.getStats());
        try {
            Assert.assertEquals("Incorrect number for bus",     (int) expectedStats.get(RouteType.BUS),         personalStatsManager.getNumberOfTripsForType(RouteType.BUS));
            Assert.assertEquals("Incorrect number for trolley", (int) expectedStats.get(RouteType.TROLLEY),     personalStatsManager.getNumberOfTripsForType(RouteType.TROLLEY));
            Assert.assertEquals("Incorrect number for tram",    (int) expectedStats.get(RouteType.TRAM),        personalStatsManager.getNumberOfTripsForType(RouteType.TRAM));
            Assert.assertEquals("Incorrect number for shuttle", (int) expectedStats.get(RouteType.SHUTTLE),     personalStatsManager.getNumberOfTripsForType(RouteType.SHUTTLE));
        }
        catch (WrongTransportType e) {
            Assert.fail("Should not fail as all transport types are correct. " + e.getMessage());
        }
    }


    @Test
    public void getForWrongType() {
        HashMap<String, Integer> expectedStats = new HashMap<>();
        expectedStats.put(RouteType.BUS,     4);
        expectedStats.put(RouteType.TRAM,    2);
        expectedStats.put(RouteType.TROLLEY, 1);
        expectedStats.put(RouteType.SHUTTLE, 5);
        mockLocalStorage.put(expectedStats);

        PersonalStatsManager personalStatsManager = new PersonalStatsManager(mockLocalStorage);

        Assert.assertEquals("Manager should return correct table", expectedStats, personalStatsManager.getStats());

        try {
            personalStatsManager.getNumberOfTripsForType("very_wrong_transport_type");
        }
        catch (WrongTransportType e) {
            Assert.assertTrue(true);
            return;
        }

        Assert.fail("Shound fail by now");
    }

    @Test
    public void setNumberForType() {
        PersonalStatsManager personalStatsManager = new PersonalStatsManager(mockLocalStorage);
        try {
            personalStatsManager.init();

            personalStatsManager.setNumberOfTripsForType(RouteType.BUS, 14);
            Assert.assertEquals("Incorrect set for bus", 14, personalStatsManager.getNumberOfTripsForType(RouteType.BUS));

            personalStatsManager.setNumberOfTripsForType(RouteType.TRAM, 16);
            Assert.assertEquals("Incorrect set for tram", 16, personalStatsManager.getNumberOfTripsForType(RouteType.TRAM));

            personalStatsManager.setNumberOfTripsForType(RouteType.TROLLEY, 12);
            Assert.assertEquals("Incorrect set for trolley", 12, personalStatsManager.getNumberOfTripsForType(RouteType.TROLLEY));

            personalStatsManager.setNumberOfTripsForType(RouteType.SHUTTLE, 144);
            Assert.assertEquals("Incorrect set for shuttle", 144, personalStatsManager.getNumberOfTripsForType(RouteType.SHUTTLE));
        }
        catch (InitPersonalStatsFailed e) {
            Assert.fail("Init should not fail. " + e.getMessage());
        }
        catch (WrongTransportType e) {
            Assert.fail("Correct transport types are used, should not fail. " + e.getMessage());
        }
    }


    @Test
    public void setNumberForTypeSaveFailing() {
        PersonalStatsManager personalStatsManager = new PersonalStatsManager(mockLocalStorage);
        try {
            personalStatsManager.init();
            mockLocalStorage.failPut = true;
            Assert.assertFalse("Save should fail", personalStatsManager.setNumberOfTripsForType(RouteType.BUS, 14));
        }
        catch (InitPersonalStatsFailed e) {
            Assert.fail("Init should not fail. " + e.getMessage());
        }
        catch (WrongTransportType e) {
            Assert.fail("Correct transport types are used, should not fail. " + e.getMessage());
        }
    }


    @Test
    public void setNumberForWrongType() {
        PersonalStatsManager personalStatsManager = new PersonalStatsManager(mockLocalStorage);
        try {
            personalStatsManager.init();

            personalStatsManager.setNumberOfTripsForType("very_vrong_transport_type", 14);
        }
        catch (InitPersonalStatsFailed e) {
            Assert.fail("Init should not fail. " + e.getMessage());
        }
        catch (WrongTransportType e) {
            Assert.assertTrue(true);
            return;
        }

        Assert.fail("Should fail by now.");
    }

    @Test
    public void incrementForType() {
        PersonalStatsManager personalStatsManager = new PersonalStatsManager(mockLocalStorage);
        HashMap<String, Integer> expectedStats = new HashMap<>();
        expectedStats.put(RouteType.BUS,     1);
        expectedStats.put(RouteType.TRAM,    0);
        expectedStats.put(RouteType.TROLLEY, 0);
        expectedStats.put(RouteType.SHUTTLE, 0);

        try {
            personalStatsManager.init();

            Assert.assertEquals("Number should have incremented", 1, personalStatsManager.incrementCounterForType(RouteType.BUS));

            Assert.assertEquals("Number should have incremented", 1, personalStatsManager.getNumberOfTripsForType(RouteType.BUS));
            Assert.assertEquals("One item should be saved", 1, mockLocalStorage.personalStatsList.size());
            Assert.assertEquals("Saved stats should be correct", expectedStats, mockLocalStorage.personalStatsList.get(0));
        }
        catch (InitPersonalStatsFailed e) {
            Assert.fail("Init should not fail. " + e.getMessage());
        }
        catch (WrongTransportType e) {
            Assert.fail("Correct transport types are used, should not fail. " + e.getMessage());
        }
    }

    @Test
    public void decrementForType() {
        PersonalStatsManager personalStatsManager = new PersonalStatsManager(mockLocalStorage);
        HashMap<String, Integer> expectedStats = new HashMap<>();
        expectedStats.put(RouteType.BUS,     4);
        expectedStats.put(RouteType.TRAM,    0);
        expectedStats.put(RouteType.TROLLEY, 0);
        expectedStats.put(RouteType.SHUTTLE, 0);

        try {
            personalStatsManager.init();
            personalStatsManager.setNumberOfTripsForType(RouteType.BUS, 5);

            Assert.assertEquals("Number should have decremented", 4, personalStatsManager.decrementNumberForType(RouteType.BUS));

            Assert.assertEquals("Number should have decremented", 4, personalStatsManager.getNumberOfTripsForType(RouteType.BUS));
            Assert.assertEquals("One item should be saved", 1, mockLocalStorage.personalStatsList.size());
            Assert.assertEquals("Saved stats should be correct", expectedStats, mockLocalStorage.personalStatsList.get(0));
        }
        catch (InitPersonalStatsFailed e) {
            Assert.fail("Init should not fail. " + e.getMessage());
        }
        catch (WrongTransportType e) {
            Assert.fail("Correct transport types are used, should not fail. " + e.getMessage());
        }
    }
}
