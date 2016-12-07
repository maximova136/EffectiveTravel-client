package com.et.tests;


import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.exception.storage.UpdateObjectFailed;
import com.et.routes.RouteType;
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
        Assert.assertEquals("Stats for type should be 0", 0, personalStatsManager.getNumberOfTripsForType(RouteType.BUS));
    }
}
