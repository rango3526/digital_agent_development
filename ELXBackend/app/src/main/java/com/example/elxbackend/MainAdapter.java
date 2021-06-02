package com.example.elxbackend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    String [] avatarDescriptions;
    int[] avatars;

    // constructor
    public MainAdapter(Context ctxt, String [] avatarDescriptions, int[] avatars){
        this.context = ctxt;
        this.avatarDescriptions = avatarDescriptions;
        this.avatars = avatars;

    }

    @Override
    public int getCount() {
        return avatarDescriptions.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // sets the view for grid
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null){
           inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.row_item, null);
        }

        ImageView imageViewGrid = convertView.findViewById(R.id.imageViewInGrid);
        TextView textViewGrid = convertView.findViewById(R.id.textViewInGrid);

        imageViewGrid.setImageResource(avatars[position]);
        textViewGrid.setText(avatarDescriptions[position]);
        return convertView;
    }
}
