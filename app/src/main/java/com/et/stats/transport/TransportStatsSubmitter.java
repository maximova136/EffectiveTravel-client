package com.et.stats.transport;


import com.et.api.IApiClient;
import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;

import java.util.Date;



public class TransportStatsSubmitter implements ITransportStatsSubmitter {
    private IApiClient client;

    public TransportStatsSubmitter(IApiClient client) {
        this.client = client;
    }

    @Override
    public void submitNote(int s_id, int r_id, Date time) throws RequestFailedException, InsuccessfulResponseException {
        client.submitNote(s_id, r_id, time.toString());
    }
}
