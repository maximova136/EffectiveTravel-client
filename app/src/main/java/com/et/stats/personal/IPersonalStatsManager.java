package com.et.stats.personal;


import com.et.exception.manager.WrongTransportType;

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
     * @throws WrongTransportType if there is no such transport type.
     * @return Number of trips by this type of transport.
     */
    public int getNumberOfTripsForType(String transportType) throws WrongTransportType;



    /**
     * Get number of trips by concrete type of public transport.
     * @param transportType Type of transport, like bus, tram, etc.
     * @param numberOfTrips Number of trips by this type of transport.
     * @return true on success.
     */
    public boolean setNumberOfTripsForType(String transportType, int numberOfTrips) throws WrongTransportType;



    /**
     * Increment by 1 number of trips by concrete type of public transport.
     * @param transportType Type of transport, like bus, tram, etc.
     * @return New value of counter, -1 in case of failure.
     */
    public int incrementCounterForType(String transportType) throws WrongTransportType;


    /**
     * Increment by 1 number of trips by concrete type of public transport.
     * @param transportType Type of transport, like bus, tram, etc.
     * @return New value of counter, -1 in case of failure.
     */
    public int decrementNumberForType(String transportType) throws WrongTransportType;
}
