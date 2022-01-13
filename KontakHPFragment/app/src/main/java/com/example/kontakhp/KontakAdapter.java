package com.example.kontakhp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class KontakAdapter extends ArrayAdapter<Kontak> {
    // View lookup cache
    public static class ViewHolder {
        TextView nama;
        TextView nohp;
    }

    public KontakAdapter(Context context, int resource, List<Kontak> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View ConvertView, ViewGroup parent) {
        Kontak dtkontak = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewKontak;

        if (ConvertView == null) {
            // If there's no view to reuse, inflate a brand new view for row
            viewKontak = new ViewHolder();
            ConvertView = LayoutInflater.from(getContext()).inflate(R.layout.item_kontak, parent, false);

            viewKontak.nama = (TextView) ConvertView.findViewById(R.id.tNama);
            viewKontak.nohp = (TextView) ConvertView.findViewById(R.id.tnoHp);

            // Cache the viewHolder object inside the fresh view
            ConvertView.setTag(viewKontak);

            // Button btn = (Button) ConvertView.findViewById(R.id.btn);
            // btn.setTag(position);
            // btn.setOnClickListener(op);
        }

        else {
            viewKontak = (ViewHolder) ConvertView.getTag();
        }

        viewKontak.nama.setText(dtkontak.getNama());
        viewKontak.nohp.setText(dtkontak.getNoHp());

        return ConvertView;
    }
}
