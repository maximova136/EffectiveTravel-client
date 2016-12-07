package com.et.stats.personal;


import com.et.storage.ILocalStorage;

import java.util.HashMap;

public class PersonalStatsStorage implements IPersonalStatsStorage {

    private ILocalStorage storage;

    public PersonalStatsStorage(ILocalStorage localStorage) {
        this.storage = localStorage;
    }


    @Override
    public boolean save() {
        return false;
    }

    @Override
    public HashMap<String, Integer> load() {
        return null;
    }
}
