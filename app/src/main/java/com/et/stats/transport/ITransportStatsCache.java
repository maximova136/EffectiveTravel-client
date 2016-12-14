package com.et.stats.transport;


import com.et.response.object.StatisticsObject;

public interface ITransportStatsCache {
    public StatisticsObject load(int s_id, int r_id);
    public void save(int s_id, int r_id, StatisticsObject statistics);
    public void clear();
}
