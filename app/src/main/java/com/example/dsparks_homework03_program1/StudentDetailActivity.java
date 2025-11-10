/*
 Name: David Sparks
 Date: 10/30/2025
 Desc: Shows details for one student.
*/

package com.example.dsparks_homework03_program1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDetailActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        // db + key
        db = new DatabaseHelper(this);
        username = getIntent().getStringExtra("username");

        // views
        final TextView tvName = findViewById(R.id.tvName);
        final TextView tvUsername = findViewById(R.id.tvUsername);
        final TextView tvEmail = findViewById(R.id.tvEmail);
        final TextView tvAge = findViewById(R.id.tvAge);
        final TextView tvGpa = findViewById(R.id.tvGpa);
        final TextView tvMajor = findViewById(R.id.tvMajor);

        Button btnBack = findViewById(R.id.btnBack);
        Button btnUpdate = findViewById(R.id.btnUpdate);

        // load student
        Student s = db.getStudentByUsername(username);
        if (s != null) {
            tvName.setText(s.getLastName() + ", " + s.getFirstName());
            tvUsername.setText(s.getUsername());
            tvEmail.setText(s.getEmail());
            tvAge.setText(String.valueOf(s.getAge()));
            tvGpa.setText(String.valueOf(s.getGpa()));

            String majorText = "None";
            if (s.getMajorId() != null) {
                Major m = db.getMajorById(s.getMajorId());
                if (m != null) {
                    majorText = m.getPrefix() + " - " + m.getName();
                }
            }
            tvMajor.setText(majorText);
        }

        // back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentDetailActivity.this, UpdateStudentActivity.class);
                i.putExtra("username", username);
                startActivity(i);
            }
        });
    }
}
