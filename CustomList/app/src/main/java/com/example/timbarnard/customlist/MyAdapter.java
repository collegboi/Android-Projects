package com.example.timbarnard.customlist;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;


/**
 * Created by Timbarnard on 08/10/2015.
 */
public class MyAdapter extends ArrayAdapter {


    List list = new ArrayList();

    static class DataHandler {

        ImageView image;
        TextView title;
        TextView detail;
    }

    public MyAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        row = convertView;
        DataHandler handler;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_layout, parent, false);

            handler = new DataHandler();
            handler.image = (ImageView)row.findViewById(R.id.image_1);
            handler.title = (TextView)row.findViewById(R.id.title_Label);
            handler.detail = (TextView)row.findViewById(R.id.detail_Label);
            row.setTag(handler);
        } else {
            handler = (DataHandler)row.getTag();
        }

        MyClass myClass;
        myClass = (MyClass)this.getItem(position);
        handler.image.setImageResource(myClass.getImage());
        handler.title.setText(myClass.getTitle());
        handler.detail.setText(myClass.getDetail());


        return row;
    }
}
