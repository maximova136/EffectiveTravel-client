package com.et.stats.personal;


import java.util.HashMap;

public class PersonalStatsManager implements IPersonalStatsManager {
    private HashMap<String, Integer> personalStats;

    private IPersonalStatsStorage storage;

    public PersonalStatsManager() {

    }

    @Override
    public HashMap<String, Integer> getStats() {
        return null;
    }

    @Override
    public int getNumberOfTripsForType(String transportType) {
        return 0;
    }
}
