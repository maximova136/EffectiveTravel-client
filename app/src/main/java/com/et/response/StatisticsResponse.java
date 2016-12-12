package com.et.response;


import java.util.List;

public class StatisticsResponse extends BaseResponse {
    private StatisticsObject statistics;

    public StatisticsObject getStatistics() {
        return statistics;
    }

    public void setStatistics(StatisticsObject statistics) {
        this.statistics = statistics;
    }
}
