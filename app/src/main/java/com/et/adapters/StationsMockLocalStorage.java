package com.et.adapters;

import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.exception.storage.UpdateObjectFailed;
import com.et.response.object.StationObject;
import com.et.stations.StationsStorage;
import com.et.storage.ILocalStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StationsMockLocalStorage implements ILocalStorage {
    public ArrayList<StationObject> list;
    public ArrayList<StationObject> savedList;

    public boolean clearDbCalled = false;

    public StationsMockLocalStorage() {
        savedList = new ArrayList<>();
        list = new ArrayList<>();
    }

    public void clear() {
        savedList.clear();
        list.clear();
        clearDbCalled = false;
    }

    public void put(StationObject station) {
        list.add(station);
    }

    @Override
    public void putObject(String collection, HashMap<String, String> values) throws PutObjectFailed {
        if(collection != StationsStorage.COLLECTION_NAME)
            throw new PutObjectFailed("Incorrect collection name");

        StationObject r = new StationObject();
        r.setS_id(Integer.parseInt(values.get(StationsStorage.S_ID_KEY)));
        r.setTitle(values.get(StationsStorage.TITLE_KEY));

        savedList.add(r);
    }

    @Override
    public void updateObject(String collection, int objectId, HashMap<String, String> values) throws UpdateObjectFailed {
        if(collection != StationsStorage.COLLECTION_NAME)
            throw new UpdateObjectFailed("Incorrect collection name");
    }

    @Override
    public void deleteObject(String collection, int objectId) throws DeleteObjectFailed {
        if(collection != StationsStorage.COLLECTION_NAME)
            throw new DeleteObjectFailed("Incorrect collection name");

    }

    @Override
    public List<HashMap<String, String>> loadCollection(String collection) throws LoadCollectionFailed {
        if(collection != StationsStorage.COLLECTION_NAME)
            throw new LoadCollectionFailed("Incorrect collection name");

        ArrayList< HashMap<String, String> > collectionList = new ArrayList<>();

        for (StationObject s : list ) {
            HashMap<String, String> item = new HashMap<>();
            item.put(StationsStorage.S_ID_KEY,  "" + s.getS_id());
            item.put(StationsStorage.TITLE_KEY, "" + new String(s.getTitle()));
            collectionList.add(item);
        }

        return collectionList;
    }

    @Override
    public void clearStorage() throws DeleteObjectFailed {
        list.clear();
        savedList.clear();
        clearDbCalled = true;
    }
}