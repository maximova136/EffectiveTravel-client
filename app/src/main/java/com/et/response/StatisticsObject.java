package com.et.response;


import com.et.response.object.FreqObject;

import java.util.List;

public class StatisticsObject {
    private List<FreqObject> weekendFreq;
    private List<FreqObject> fridayFreq;
    private List<FreqObject> weekdaysFreq;

    public boolean expired;

    public List<FreqObject> getWeekendFreq() {
        return weekendFreq;
    }

    public void setWeekendFreq(List<FreqObject> weekendFreq) {
        this.weekendFreq = weekendFreq;
    }

    public List<FreqObject> getFridayFreq() {
        return fridayFreq;
    }

    public void setFridayFreq(List<FreqObject> fridayFreq) {
        this.fridayFreq = fridayFreq;
    }

    public List<FreqObject> getWeekdaysFreq() {
        return weekdaysFreq;
    }

    public void setWeekdaysFreq(List<FreqObject> weekdaysFreq) {
        this.weekdaysFreq = weekdaysFreq;
    }
}
