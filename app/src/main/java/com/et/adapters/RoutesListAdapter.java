package com.et.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.et.R;
import com.et.response.object.RouteObject;
import com.et.response.object.StationObject;
import com.et.stations.StationsList;

import java.util.List;


public class RoutesListAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater lInflater;
    private List<RouteObject> routes;

    public RoutesListAdapter(Context context, List<RouteObject> _routes) {
        ctx = context;
        routes = _routes;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return routes.size();
    }

    // элемент по позиции
    @Override
    public RouteObject getItem(int position) {
        return routes.get(position);
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
            view = lInflater.inflate(R.layout.routes_list_item, parent, false);
        }

        RouteObject route = getItem(position);

        // заполняем View в пункте списка данными
        ((TextView) view.findViewById(R.id.RouteItem)).setText(route.getTitle()+" ("+route.getTransport_type()+")");

        return view;
    }

}
