package com.et.stats.transport;


import com.et.response.StatisticsResponse;
import com.et.response.object.FreqObject;

import java.util.Date;
import java.util.List;

public interface ITransportStatsManager {
    public StatisticsResponse getStats(int s_id, int r_id);
    public boolean submitNote(int s_id, int r_id, Date time);
}
