package com.et.stats.personal;


import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.storage.ILocalStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalStatsStorage implements IPersonalStatsStorage {
    public static final String COLLECTION_NAME = "personal_stats";

    private ILocalStorage storage;

    public PersonalStatsStorage(ILocalStorage localStorage) {
        this.storage = localStorage;
    }

    @Override
    public boolean save(HashMap<String, Integer> personalStats) {
        try {
            storage.clearCollection(COLLECTION_NAME);

            HashMap<String, String> item = new HashMap<>();
            for(Map.Entry<String, Integer> pair : personalStats.entrySet() ) {
                item.put(pair.getKey(), pair.getValue().toString());
            }

            storage.putObject(COLLECTION_NAME, item);

            return true;
        }
        catch (DeleteObjectFailed e) {
            return false;
        }
        catch (PutObjectFailed e) {
            return false;
        }
    }

    @Override
    public HashMap<String, Integer> load() {
        try {
            HashMap<String, Integer> loadedStats = new HashMap<>();
            List< HashMap<String, String> > collection = storage.loadCollection(COLLECTION_NAME);

            if(collection == null || collection.size() <= 0)
                return null;

            HashMap<String, String> item = collection.get(0);

            for(Map.Entry<String, String> pair : item.entrySet() ) {
                loadedStats.put(pair.getKey(), Integer.parseInt(pair.getValue()));
            }

            return loadedStats;
        }
        catch (NumberFormatException e) {
            return null;
        }
        catch (LoadCollectionFailed e) {
            return null;
        }
    }
}
