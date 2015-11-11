package info.androidhive.materialdesign.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.model.FilterClass;

/**
 * Created by Timbarnard on 25/10/2015.
 */
public class FilterAdapter extends ArrayAdapter<FilterClass> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<FilterClass> listValues = new ArrayList<>();

    public FilterAdapter(Context mContext, int layoutResourceId, ArrayList<FilterClass> listValues) {
        super(mContext, layoutResourceId, listValues);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.listValues = listValues;
    }


    public void setGridData(ArrayList<FilterClass> listValues) {
        this.listValues = listValues;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.title_Label);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        FilterClass item = listValues.get(position);
        holder.titleTextView.setText(item.getName());

        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
    }
}