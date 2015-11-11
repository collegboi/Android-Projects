package info.androidhive.materialdesign.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.model.UserCarClass;

/**
 * Created by Timbarnard on 24/10/2015.
 */
public class UserCarAdapter extends ArrayAdapter {

    static class DataHandler {
        TextView reg;
        ImageView imageView;
    }

    Context mContext;
    List list = new ArrayList();

    public UserCarAdapter(Context context, int resource) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final DataHandler handler;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.user_row_layout, parent, false);

            handler = new DataHandler();
            handler.reg = (TextView) row.findViewById(R.id.title_Label);
            handler.imageView = (ImageView) row.findViewById(R.id.car_image);
            row.setTag(handler);
        } else {
            handler = (DataHandler) row.getTag();
        }

        UserCarClass userCarClass;
        userCarClass = (UserCarClass) this.getItem(position);
        handler.reg.setText(userCarClass.getCarReg());
        handler.imageView.setImageBitmap(decodeBase64(userCarClass.getCarPhoto().get(0)));

//        Picasso.with(mContext)
//                .load(userCarClass.getCarPhoto().get(0))
//                .into(new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        handler.imageView.setImageBitmap(bitmap);
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Drawable errorDrawable) {
//                       // handler.imageView.setImageResource(R.drawable.ic_error);
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                });


        return row;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
