package com.et.response.object;




public class FreqObject {
    private String time;
    private float count;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public String toString() {
        return "{" + time + "->" + count + "}";
    }
}
