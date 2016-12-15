package com.et.stats.personal;


import com.et.exception.manager.InitPersonalStatsFailed;
import com.et.exception.manager.WrongTransportType;
import com.et.routes.RouteType;
import com.et.storage.ILocalStorage;

import java.util.HashMap;

public class PersonalStatsManager implements IPersonalStatsManager {
    private HashMap<String, Integer> personalStats;

    private IPersonalStatsStorage storage;

    public PersonalStatsManager(ILocalStorage localStorage)  {
        storage = new PersonalStatsStorage(localStorage);
        personalStats = storage.load();

        if(personalStats == null) {
            personalStats = new HashMap<>();
        }
    }


    public void init() throws InitPersonalStatsFailed {
        personalStats.put(RouteType.BUS,     0);
        personalStats.put(RouteType.TRAM,    0);
        personalStats.put(RouteType.TROLLEY, 0);
        personalStats.put(RouteType.SHUTTLE, 0);
        if(!storage.save(personalStats)) {
            throw new InitPersonalStatsFailed("Failed to save brand new counters");
        }
    }


    @Override
    public HashMap<String, Integer> getStats() {
        return personalStats;
    }


    @Override
    public int getNumberOfTripsForType(String transportType) throws WrongTransportType {
        if(!checkTransportType(transportType))
            throw new WrongTransportType("No such transport type '" + transportType + "'");

        Integer numberOfTrips = personalStats.get(transportType);
        if(numberOfTrips == null) {
            personalStats.put(transportType, 0);
            return 0;
        }

        return numberOfTrips;
    }


    @Override
    public boolean setNumberOfTripsForType(String transportType, int numberOfTrips) throws WrongTransportType {
        if(!checkTransportType(transportType))
            throw new WrongTransportType("No such transport type '" + transportType + "'");

        int oldValue = getNumberOfTripsForType(transportType);
        personalStats.put(transportType, numberOfTrips);
        if(!storage.save(personalStats)) {
            personalStats.put(transportType, oldValue);
            return false;
        }

        return true;
    }


    @Override
    public int incrementCounterForType(String transportType) throws WrongTransportType {
        int newCounterValue = getNumberOfTripsForType(transportType) + 1;

        if(!setNumberOfTripsForType(transportType, newCounterValue)) {
            return -1;
        }

        return newCounterValue;
    }


    @Override
    public int decrementNumberForType(String transportType) throws WrongTransportType {
        int newCounterValue = getNumberOfTripsForType(transportType) - 1;

        if(newCounterValue < 0)
            return 0;

        if(!setNumberOfTripsForType(transportType, newCounterValue)) {
            return -1;
        }

        return newCounterValue;
    }

    private boolean checkTransportType(String type) {
        if(type.equals(RouteType.BUS))
            return true;

        if(type.equals(RouteType.TRAM))
            return true;

        if(type.equals(RouteType.TROLLEY))
            return true;

        if(type.equals(RouteType.SHUTTLE))
            return true;

        return false;
    }
}
