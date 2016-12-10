package com.et.tests;


import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.exception.storage.UpdateObjectFailed;
import com.et.response.object.RouteObject;
import com.et.routes.RouteStorage;
import com.et.stats.personal.PersonalStatsStorage;
import com.et.storage.ILocalStorage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPersonalStatsStorage {

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


    private MockLocalStorage mockLocalStorage;



    public TestPersonalStatsStorage() {
        mockLocalStorage = new MockLocalStorage();
    }

    @Before
    public void setUpTest() {
        mockLocalStorage.clear();
    }

    @Test
    public void savePersonalStats() {
        PersonalStatsStorage personalStatsStorage = new PersonalStatsStorage(mockLocalStorage);
        HashMap<String, Integer> personalStats = new HashMap<>();

        personalStats.put("test_type_1", 33);
        personalStats.put("test_type_2", 62);
        personalStats.put("test_type_3", 14);
        personalStats.put("test_type_4", 23);

        Assert.assertTrue("Save should be successful", personalStatsStorage.save(personalStats));
        Assert.assertTrue("Collection should be cleared before save", mockLocalStorage.clearDbCalled);
        Assert.assertEquals("Only one object should be saved", 1, mockLocalStorage.personalStatsList.size());
        Assert.assertEquals("Saved object should be the same", personalStats, mockLocalStorage.personalStatsList.get(0));
    }


    @Test
    public void loadPersonalStats() {
        PersonalStatsStorage personalStatsStorage = new PersonalStatsStorage(mockLocalStorage);
        HashMap<String, Integer> personalStats = new HashMap<>();

        personalStats.put("test_type_1", 33);
        personalStats.put("test_type_2", 62);
        personalStats.put("test_type_3", 14);
        personalStats.put("test_type_4", 23);

        mockLocalStorage.put(personalStats);

        HashMap<String, Integer> loadedStats = personalStatsStorage.load();

        Assert.assertEquals("Loaded row should be equal to original", personalStats, loadedStats);
    }

    @Test
    public void saveThenLoad() {
        PersonalStatsStorage personalStatsStorage = new PersonalStatsStorage(mockLocalStorage);
        HashMap<String, Integer> personalStats = new HashMap<>();

        personalStats.put("test_type_1", 33);
        personalStats.put("test_type_2", 62);
        personalStats.put("test_type_3", 14);
        personalStats.put("test_type_4", 23);

        Assert.assertTrue("Save should be successful", personalStatsStorage.save(personalStats));
        HashMap<String, Integer> loadedStats = personalStatsStorage.load();
        Assert.assertEquals("Loaded row should be equal to original", personalStats, loadedStats);
    }


    @Test
    public void saveFailingPut() {
        PersonalStatsStorage personalStatsStorage = new PersonalStatsStorage(mockLocalStorage);
        HashMap<String, Integer> personalStats = new HashMap<>();

        personalStats.put("test_type_1", 33);
        personalStats.put("test_type_2", 62);
        personalStats.put("test_type_3", 14);
        personalStats.put("test_type_4", 23);

        mockLocalStorage.failPut = true;

        Assert.assertFalse("Save should not be successful", personalStatsStorage.save(personalStats));
    }


    @Test
    public void saveFailingClear() {
        PersonalStatsStorage personalStatsStorage = new PersonalStatsStorage(mockLocalStorage);
        HashMap<String, Integer> personalStats = new HashMap<>();

        personalStats.put("test_type_1", 33);
        personalStats.put("test_type_2", 62);
        personalStats.put("test_type_3", 14);
        personalStats.put("test_type_4", 23);

        mockLocalStorage.failDelete = true;

        Assert.assertFalse("Save should not be successful", personalStatsStorage.save(personalStats));
    }

    @Test
    public void loadEmpty() {
        PersonalStatsStorage personalStatsStorage = new PersonalStatsStorage(mockLocalStorage);

        HashMap<String, Integer> loadedStats = personalStatsStorage.load();

        Assert.assertEquals("Loaded row should be equal to original", null, loadedStats);
    }


    @Test
    public void loadFailing() {
        PersonalStatsStorage personalStatsStorage = new PersonalStatsStorage(mockLocalStorage);

        HashMap<String, Integer> personalStats = new HashMap<>();

        personalStats.put("test_type_1", 33);
        personalStats.put("test_type_2", 62);
        personalStats.put("test_type_3", 14);
        personalStats.put("test_type_4", 23);

        mockLocalStorage.failLoad = true;

        HashMap<String, Integer> loadedStats = personalStatsStorage.load();

        Assert.assertEquals("Loaded row should be equal to original", null, loadedStats);
    }
}
