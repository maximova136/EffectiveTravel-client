package com.et.adapters;

import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.exception.storage.UpdateObjectFailed;
import com.et.response.object.RouteObject;
import com.et.routes.RouteStorage;
import com.et.storage.ILocalStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoutesMockLocalStorage implements ILocalStorage {
    public ArrayList<RouteObject> list;
    public ArrayList<RouteObject> savedList;

    public boolean clearDbCalled = false;

    public RoutesMockLocalStorage() {
        savedList = new ArrayList<>();
        list = new ArrayList<>();
    }

    public void clear() {
        savedList.clear();
        list.clear();
        clearDbCalled = false;
    }

    public void put(RouteObject route) {
        list.add(route);
    }

    @Override
    public void putObject(String collection, HashMap<String, String> values) throws PutObjectFailed {
        if(collection != RouteStorage.COLLECTION_NAME)
            throw new PutObjectFailed("Incorrect collection name");

        RouteObject r = new RouteObject();
        r.setR_id(Integer.parseInt(values.get(RouteStorage.R_ID_KEY)));
        r.setTransport_type(values.get(RouteStorage.TYPE_KEY));
        r.setTitle(values.get(RouteStorage.TITLE_KEY));
        r.setCost(Integer.parseInt(values.get(RouteStorage.COST_KEY)));

        savedList.add(r);
    }

    @Override
    public void updateObject(String collection, int objectId, HashMap<String, String> values) throws UpdateObjectFailed {
        if(collection != RouteStorage.COLLECTION_NAME)
            throw new UpdateObjectFailed("Incorrect collection name");
    }

    @Override
    public void deleteObject(String collection, int objectId) throws DeleteObjectFailed {
        if(collection != RouteStorage.COLLECTION_NAME)
            throw new DeleteObjectFailed("Incorrect collection name");

    }

    @Override
    public void clearStorage() throws DeleteObjectFailed {
        list.clear();
        savedList.clear();
        clearDbCalled = true;
    }

    @Override
    public List<HashMap<String, String>> loadCollection(String collection) throws LoadCollectionFailed {
        if(collection != RouteStorage.COLLECTION_NAME)
            throw new LoadCollectionFailed("Incorrect collection name");

        ArrayList< HashMap<String, String> > collectionList = new ArrayList<>();

        for (RouteObject r : list ) {
            HashMap<String, String> item = new HashMap<>();
            item.put(RouteStorage.R_ID_KEY,  "" + r.getR_id());
            item.put(RouteStorage.TYPE_KEY,  "" + new String(r.getTransport_type()));
            item.put(RouteStorage.TITLE_KEY, "" + new String(r.getTitle()));
            item.put(RouteStorage.COST_KEY,  "" + r.getCost());
            collectionList.add(item);
        }

        return collectionList;
    }
}