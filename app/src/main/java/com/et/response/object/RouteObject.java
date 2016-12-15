package com.et.response.object;


import android.os.Parcel;
import android.os.Parcelable;

public class RouteObject implements Parcelable {
    int r_id;
    String title;
    int cost;
    String transport_type;

    public RouteObject() {}

    private RouteObject(Parcel in) {
        this.r_id = in.readInt();
        this.title = in.readString();
        this.cost = in.readInt();
        this.transport_type = in.readString();
    }

    public int getR_id() {
        return r_id;
    }

    public void setR_id(int r_id) {
        this.r_id = r_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getTransport_type() {
        return transport_type;
    }

    public void setTransport_type(String transport_type) {
        this.transport_type = transport_type;
    }

    public String toString() { return "r_id:" + r_id + " Type:" + transport_type + " Title:" + title + " Cost:" + cost; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(r_id);
        parcel.writeString(title);
        parcel.writeInt(cost);
        parcel.writeString(transport_type);
    }

    public static final Parcelable.Creator<RouteObject> CREATOR = new Parcelable.Creator<RouteObject>() {

        public RouteObject createFromParcel(Parcel in) {
            return new RouteObject(in);
        }

        public RouteObject[] newArray(int size) {
            return new RouteObject[size];
        }
    };
}
