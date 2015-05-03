package com.ahmedbilal.realim;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListCustomAdapter extends ArrayAdapter<CustomAdapterData> {

    public ListCustomAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListCustomAdapter(Context context, int resource, List<CustomAdapterData> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listitemrows, null);

        }

        CustomAdapterData p = getItem(position);

        if (p != null) {

            TextView tt = (TextView) v.findViewById(R.id.listUserName);
            TextView tt1 = (TextView) v.findViewById(R.id.listMessage);
            ImageView imgView = (ImageView) v.findViewById(R.id.listImage );

            if (tt != null) {
                tt.setText(p.getUserName());
            }
            if (tt1 != null) {

                tt1.setText(p.getMessage());
            }
            if (imgView != null) {
                imgView.setImageBitmap(p.getImageBitmap());
            }
        }

        return v;

    }
}