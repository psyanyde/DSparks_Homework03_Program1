/*
 Name: David Sparks
 Date: 10/30/2025
 Desc: Basic ListView adapter for students on the main screen.
*/

package com.example.dsparks_homework03_program1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class StudentListAdapter extends BaseAdapter {
    private List<Student> data;
    private LayoutInflater inflater;

    public StudentListAdapter(Context ctx, List<Student> list) {
        data = list;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() { return data.size(); }

    @Override
    public Object getItem(int position) { return data.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_student, parent, false);
            vh = new ViewHolder();
            vh.name = (TextView) convertView.findViewById(R.id.tvName);
            vh.username = (TextView) convertView.findViewById(R.id.tvUsername);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Student s = data.get(position);
        vh.name.setText(s.getLastName() + ", " + s.getFirstName());
        vh.username.setText(s.getUsername());
        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView username;
    }
}
