package com.et.stats.transport;


import com.et.exception.api.InsuccessfulResponseException;
import com.et.exception.api.RequestFailedException;

import java.util.Date;

public interface ITransportStatsSubmitter {
    public void submitNote(int s_id, int r_id, Date time) throws RequestFailedException, InsuccessfulResponseException;
}
