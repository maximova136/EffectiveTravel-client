package com.et.stats.transport;


import com.et.response.StatisticsObject;

import java.util.Date;

public interface ITransportStatsManager {
    public StatisticsObject getStats(int s_id, int r_id);
    public boolean submitNote(int s_id, int r_id, Date time);
}
