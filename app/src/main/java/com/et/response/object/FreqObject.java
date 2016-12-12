package com.et.response.object;




public class FreqObject {
    private String time;
    private double count;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public String toString() {
        return "{" + time + "->" + count + "}";
    }
}
