package com.et.stats.transport;


import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.response.StatisticsResponse;

public interface ITransportStatsFetcher {
    public StatisticsResponse fetch(int s_id, int r_id) throws RequestFailedException, InsuccessfulResponseException;
}
