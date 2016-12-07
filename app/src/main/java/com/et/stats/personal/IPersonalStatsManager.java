package com.et.stats.personal;


import java.util.HashMap;

public interface IPersonalStatsManager {
    public HashMap<String, Integer> getStats();
    public int getNumberOfTripsForType(String transportType);
}
