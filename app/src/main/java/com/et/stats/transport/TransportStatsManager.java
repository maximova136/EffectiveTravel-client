package com.et.stats.transport;


import com.et.api.IApiClient;
import com.et.exception.api.ApiCallException;
import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.response.StatisticsObject;
import com.et.storage.ISQLiteDb;

import java.util.Date;

public class TransportStatsManager implements ITransportStatsManager {
    private ITransportStatsFetcher fetcher;
    private ITransportStatsCache cache;
    private ITransportStatsSubmitter submitter;

    public TransportStatsManager(IApiClient apiClient, ISQLiteDb sqLiteDb) {
        fetcher = new TransportStatsFetcher(apiClient);
        cache = new TransportStatsCache(sqLiteDb);
        submitter = new TransportStatsSubmitter(apiClient);
    }

    @Override
    public StatisticsObject getStats(int s_id, int r_id) {
        StatisticsObject stats =  cache.load(s_id, r_id);

        if(stats == null) {
            try {
                stats = fetcher.fetch(s_id, r_id);
                cache.save(s_id, r_id, stats);

            }
            catch (RequestFailedException e) {
                return null;
            }
            catch (InsuccessfulResponseException e) {
                return null;
            }
        }

        return stats;
    }

    @Override
    public boolean submitNote(int s_id, int r_id, Date time) {
        try {
            submitter.submitNote(s_id, r_id, time);
            return true;
        }
        catch (ApiCallException e) {
            return false;
        }
    }
}
