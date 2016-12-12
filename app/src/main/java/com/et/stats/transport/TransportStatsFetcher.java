package com.et.stats.transport;


import com.et.api.IApiClient;
import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.response.StatisticsResponse;

public class TransportStatsFetcher implements ITransportStatsFetcher {
    IApiClient client;

    public TransportStatsFetcher(IApiClient client) {
        this.client = client;
    }

    @Override
    public StatisticsResponse fetch(int s_id, int r_id) throws RequestFailedException, InsuccessfulResponseException {
        return client.statistics(s_id, r_id);
    }
}
