package com.et.response.object;


import java.util.List;

public class StationObject {
    protected int s_id;
    protected String title;
    protected List<Integer> routes;

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Integer> routes) {
        this.routes = routes;
    }

    public String toString() {
        return "[" + s_id + "] " + title + " {" + routes.toString() + "}";
    }
}
