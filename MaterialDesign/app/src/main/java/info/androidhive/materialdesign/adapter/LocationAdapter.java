package info.androidhive.materialdesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.model.UserCarClass;

/**
 * Created by Timbarnard on 08/11/15.
 */
public class LocationAdapter extends ArrayAdapter {

    static class Handler {
        TextView location;
        TextView date;
    }
    Context mContext;
    List list = new ArrayList();

    public LocationAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
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
    public void remove(Object object) {
        super.remove(object);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final Handler handler;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.location_row_layout, parent, false);

            handler = new Handler();
            handler.location = (TextView) row.findViewById(R.id.loc_Label);
            handler.date = (TextView) row.findViewById(R.id.date_label);
            row.setTag(handler);
        } else {
            handler = (Handler) row.getTag();
        }

        UserCarClass.Location location;
        location = (UserCarClass.Location) this.getItem(position);
        handler.location.setText(location.getLocTown().toString()+",\n"+location.getLocCounty().toString()+",\n"+location.getLocCountry().toString());
        handler.date.setText(location.getLocDate());

        return row;
    }

}
