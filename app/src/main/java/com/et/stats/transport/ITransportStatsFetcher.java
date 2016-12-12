package com.et.stats.transport;


import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;
import com.et.response.StatisticsObject;

public interface ITransportStatsFetcher {
    public StatisticsObject fetch(int s_id, int r_id) throws RequestFailedException, InsuccessfulResponseException;
}
