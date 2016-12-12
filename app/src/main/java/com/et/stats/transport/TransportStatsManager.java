package com.et.stats.transport;


import com.et.api.IApiClient;
import com.et.exception.api.ApiCallException;
import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.response.StatisticsResponse;
import com.et.response.object.FreqObject;
import com.et.storage.ILocalStorage;

import java.util.Date;
import java.util.List;

public class TransportStatsManager implements ITransportStatsManager {
    private ITransportStatsFetcher fetcher;
    private ITransportStatsCache cache;
    private ITransportStatsSubmitter submitter;

    private List<FreqObject> weekendStats;
    private List<FreqObject> fridayStats;
    private List<FreqObject> weekdayStats;

    public TransportStatsManager(IApiClient apiClient, ILocalStorage localStorage) {
        fetcher = new TransportStatsFetcher(apiClient);
    }

    @Override
    public StatisticsResponse getStats(int s_id, int r_id) {
        StatisticsResponse stats = cache.load(s_id, r_id);

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
