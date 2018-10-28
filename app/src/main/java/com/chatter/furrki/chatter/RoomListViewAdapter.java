package com.chatter.furrki.chatter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;

public class RoomListViewAdapter extends ArrayAdapter<ParseUser> {

    private final LayoutInflater inflater;
    private final Context context;
    private ViewHolder holder;
    private final ArrayList<ParseUser> persons;

    public RoomListViewAdapter(Context context, ArrayList<ParseUser> persons) {
        super(context,0, persons);
        this.context = context;
        this.persons = persons;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public ParseUser getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return persons.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.room_list_item, null);

            holder = new ViewHolder();
            holder.userimage = (ImageView) convertView.findViewById(R.id.user_image);
            holder.username = (TextView) convertView.findViewById(R.id.user_name);
            convertView.setTag(holder);

        }
        else{
            //Get viewholder we already created
            holder = (ViewHolder)convertView.getTag();
        }

        ParseUser person = persons.get(position);
        if(person != null){
            //holder.userimage.setImageResource();
            holder.username.setText(person.getUsername());

        }
        return convertView;
    }

    //View Holder Pattern for better performance
    private static class ViewHolder {
        TextView username;
        ImageView userimage;

    }
}