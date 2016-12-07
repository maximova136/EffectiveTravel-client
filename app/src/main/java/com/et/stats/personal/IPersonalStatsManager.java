package com.et.stats.personal;


import java.util.HashMap;

public interface IPersonalStatsManager {

    /**
     * Get all personal stats counters.
     * @return HashMap with items like transport_type [key] => number_of_trips [value].
     */
    public HashMap<String, Integer> getStats();



    /**
     * Get number of trips by concrete type of public transport.
     * @param transportType Type of transport, like bus, tram, etc.
     * @return Number of trips by this type of transport.
     */
    public int getNumberOfTripsForType(String transportType);



    /**
     * Get number of trips by concrete type of public transport.
     * @param transportType Type of transport, like bus, tram, etc.
     * @param numberOfTrips Number of trips by this type of transport.
     * @return true on success.
     */
    public boolean setNumberOfTripsForType(String transportType, int numberOfTrips);



    /**
     * Increment by 1 number of trips by concrete type of public transport.
     * @param transportType Type of transport, like bus, tram, etc.
     * @return New value of counter, -1 in case of failure.
     */
    public int incrementCounterForType(String transportType);


    /**
     * Increment by 1 number of trips by concrete type of public transport.
     * @param transportType Type of transport, like bus, tram, etc.
     * @return New value of counter, -1 in case of failure.
     */
    public int decrementNumberForType(String transportType);
}
