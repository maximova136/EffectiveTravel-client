package com.et.stats.transport;

import com.et.interfaces.ITransportStatsManager;




public class TransportStatsManager implements ITransportStatsManager {
    private ITransportStatsFetcher fetcher;
    private ITransportStatsCache cache;
    private ITransportStatsSubmitter submitter;

}
