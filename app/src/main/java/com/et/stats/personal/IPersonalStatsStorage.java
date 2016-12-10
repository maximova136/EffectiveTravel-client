package com.et.stats.personal;


import java.util.HashMap;

public interface IPersonalStatsStorage {

    /**
     * Save personal stats counters to local DB.
     * @param personalStats HashMap with items like transport_type[key] => number_of_trips[value]
     * @return true on success
     */
    public boolean save(HashMap<String, Integer> personalStats);



    /**
     * Load personal stats counters from local DB.
     * @return HashMap with items like transport_type[key] => number_of_trips[value]
     */
    public HashMap<String, Integer> load();
}
