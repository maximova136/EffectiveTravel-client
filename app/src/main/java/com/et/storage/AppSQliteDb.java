package com.et.storage;


import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.exception.storage.UpdateObjectFailed;

import java.util.HashMap;
import java.util.List;

public class AppSQliteDb implements ILocalStorage {
    @Override
    public void clearStorage() throws DeleteObjectFailed {

    }

    @Override
    public void putObject(String collection, HashMap<String, String> values) throws PutObjectFailed {

    }

    @Override
    public void updateObject(String collection, int objectId, HashMap<String, String> values) throws UpdateObjectFailed {

    }

    @Override
    public void deleteObject(String collection, int objectId) throws DeleteObjectFailed {

    }

    @Override
    public List<HashMap<String, String>> loadCollection(String collection) throws LoadCollectionFailed {
        return null;
    }
}
