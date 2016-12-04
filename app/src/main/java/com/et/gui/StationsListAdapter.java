package com.et.gui;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.et.R;
import com.et.response.object.StationObject;
import com.et.stations.StationsList;



public class StationsListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    StationsList stations;

    StationsListAdapter(Context context, StationsList _stations) {
        ctx = context;
        stations = _stations;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return stations.getAll().size();
    }

    // элемент по позиции
    @Override
    public StationObject getItem(int position) {
        return stations.getAll().get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.stations_list_item, parent, false);
        }

        StationObject station = getItem(position);

        // заполняем View в пункте списка данными
        ((TextView) view.findViewById(R.id.StationTitle)).setText(station.getTitle());

        return view;
    }

}
