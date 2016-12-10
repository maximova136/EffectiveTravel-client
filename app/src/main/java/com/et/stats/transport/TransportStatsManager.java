package com.et.stats.transport;



public class TransportStatsManager implements ITransportStatsManager {
    private ITransportStatsFetcher fetcher;
    private ITransportStatsCache cache;
    private ITransportStatsSubmitter submitter;

}
