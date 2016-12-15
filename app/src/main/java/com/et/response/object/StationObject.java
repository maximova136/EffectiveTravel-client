package com.et.response.object;


import android.os.Parcel;
import android.os.Parcelable;

public class StationObject implements Parcelable {
import java.util.List;

public class StationObject {
    protected int s_id;
    protected String title;
    protected List<Integer> routes;

    public StationObject() {}

    private StationObject(Parcel in) {
        this.s_id = in.readInt();
        this.title = in.readString();
    }

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

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(s_id);
        parcel.writeString(title);
    }

    public static final Parcelable.Creator<StationObject> CREATOR = new Parcelable.Creator<StationObject>() {

        public StationObject createFromParcel(Parcel in) {
            return new StationObject(in);
        }

        public StationObject[] newArray(int size) {
            return new StationObject[size];
        }
    };

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
