package com.et.stations;


import com.et.exception.storage.DeleteObjectFailed;
import com.et.exception.storage.LoadCollectionFailed;
import com.et.exception.storage.PutObjectFailed;
import com.et.response.object.StationObject;
import com.et.storage.ILocalStorage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StationsStorage implements IStationsStorage {
    public static final String COLLECTION_NAME = "stations";

    public static final String S_ID_KEY = "s_id";
    public static final String TITLE_KEY = "title";
    public static final String ROUTES_KEY = "routes";

    public static final String ROUTE_SEP = ";";

    private ILocalStorage storage;

    public StationsStorage(ILocalStorage storage) {
        this.storage = storage;
    }

    @Override
    public boolean save(List<StationObject> stations) {
        try {
            storage.clearCollection(COLLECTION_NAME);

            for (StationObject s : stations) {
                HashMap<String, String> item = new HashMap<>();
                item.put(S_ID_KEY,  "" + s.getS_id());
                item.put(TITLE_KEY, "" + s.getTitle());
                item.put(ROUTES_KEY, routesListToString(s.getRoutes()));
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
                s.setRoutes(stringToRoutesList(item.get(ROUTES_KEY)));
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

    public static String routesListToString(List<Integer> routes) {
        StringBuilder routesString = new StringBuilder();

        for(Integer rId : routes) {
            routesString.append(rId.toString());
            routesString.append(ROUTE_SEP);
        }

        return routesString.toString();
    }

    public static List<Integer> stringToRoutesList(String routesString) {
        String[] parts = routesString.split(ROUTE_SEP);
        ArrayList<Integer> routes = new ArrayList<>();

        for(String rIdString : parts) {
            if(rIdString == null | rIdString.length() <= 0)
                continue;
            try {
                Integer rId = Integer.parseInt(rIdString);
                routes.add(rId);
            }
            catch (NumberFormatException e) { continue; }
        }

        return routes;
    }
}
