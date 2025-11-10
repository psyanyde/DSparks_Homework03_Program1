/*
 Name: David Sparks
 Date: 10/30/2025
 Desc: Adapter that shows full student info in the search results.
*/

package com.example.dsparks_homework03_program1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class StudentSearchAdapter extends BaseAdapter {
    private List<Student> data;
    private LayoutInflater inflater;
    private DatabaseHelper db;

    public StudentSearchAdapter(Context ctx, List<Student> list, DatabaseHelper database) {
        data = list;
        inflater = LayoutInflater.from(ctx);
        db = database;
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
            convertView = inflater.inflate(R.layout.list_item_student_full, parent, false);
            vh = new ViewHolder();
            vh.name = (TextView) convertView.findViewById(R.id.tvName);
            vh.username = (TextView) convertView.findViewById(R.id.tvUsername);
            vh.email = (TextView) convertView.findViewById(R.id.tvEmail);
            vh.age = (TextView) convertView.findViewById(R.id.tvAge);
            vh.gpa = (TextView) convertView.findViewById(R.id.tvGpa);
            vh.major = (TextView) convertView.findViewById(R.id.tvMajor);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Student s = data.get(position);
        vh.name.setText(s.getLastName() + ", " + s.getFirstName());
        vh.username.setText(s.getUsername());
        vh.email.setText(s.getEmail());
        vh.age.setText(String.valueOf(s.getAge()));
        vh.gpa.setText(String.valueOf(s.getGpa()));

        String majorText = "None";
        if (s.getMajorId() != null) {
            Major m = db.getMajorById(s.getMajorId());
            if (m != null) {
                majorText = m.getPrefix() + " - " + m.getName();
            }
        }
        vh.major.setText(majorText);

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView username;
        TextView email;
        TextView age;
        TextView gpa;
        TextView major;
    }
}
