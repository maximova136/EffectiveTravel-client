package com.et.stats.transport;


import com.et.api.IApiClient;
import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransportStatsSubmitter implements ITransportStatsSubmitter {
    private IApiClient client;
    private SimpleDateFormat formatter;

    public TransportStatsSubmitter(IApiClient client) {
        this.client = client;
        formatter = new SimpleDateFormat("H:mm");
    }

    @Override
    public void submitNote(int s_id, int r_id, Date time) throws RequestFailedException, InsuccessfulResponseException {
        client.submitNote(s_id, r_id, formatter.format(time));
    }
}
