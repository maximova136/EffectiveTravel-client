package com.et.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.et.R;
import com.et.response.object.StationObject;
import com.et.stations.StationsList;

import java.util.List;


public class StationsListAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater lInflater;
    private List<StationObject> stations;

    public StationsListAdapter(Context context, List<StationObject> _stations) {
        ctx = context;
        stations = _stations;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return stations.size();
    }

    // элемент по позиции
    @Override
    public StationObject getItem(int position) {
        return stations.get(position);
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
