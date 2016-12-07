package com.et.stats.personal;


import java.util.HashMap;

public interface IPersonalStatsStorage {
    public boolean save(HashMap<String, Integer> personalStats);
    public HashMap<String, Integer> load();
}
