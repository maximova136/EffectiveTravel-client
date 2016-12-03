package com.et.stations;


import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.response.object.StationObject;
import com.et.storage.ILocalStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StationsStorage implements IStationsStorage {
    public static String COLLECTION_NAME = "stations";

    public static String S_ID_KEY = "s_id";
    public static String TITLE_KEY = "title";

    private ILocalStorage storage;

    public StationsStorage(ILocalStorage storage) {
        this.storage = storage;
    }

    @Override
    public boolean save(List<StationObject> stations) {
        try {
            storage.clearStorage();

            for (StationObject s : stations) {
                HashMap<String, String> item = new HashMap<>();
                item.put(S_ID_KEY,  "" + s.getS_id());
                item.put(TITLE_KEY, "" + s.getTitle());
                storage.putObject(COLLECTION_NAME, item);
            }

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
    public List<StationObject> load() {
        try {
            ArrayList<StationObject> stations = new ArrayList<>();
            List< HashMap<String, String> > collection = storage.loadCollection(COLLECTION_NAME);

            if(collection == null || collection.size() <= 0)
                return null;

            for (HashMap<String, String> item : collection ) {
                StationObject s = new StationObject();
                s.setS_id(Integer.parseInt(item.get(S_ID_KEY)));
                s.setTitle(item.get(TITLE_KEY));
                stations.add(s);
            }

            return stations;
        }
        catch (NumberFormatException e) {
            return null;
        }
        catch (LoadCollectionFailed e) {
            return null;
        }
    }
}
