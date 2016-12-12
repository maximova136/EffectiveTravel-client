package com.et.stats.transport;


import com.et.response.StatisticsResponse;

public interface ITransportStatsCache {
    public StatisticsResponse load(int s_id, int r_id);
    public void save(int s_id, int r_id, StatisticsResponse statistics);
    public void clear();
}
