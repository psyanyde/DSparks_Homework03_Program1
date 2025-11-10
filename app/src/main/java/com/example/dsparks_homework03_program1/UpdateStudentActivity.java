/*
 Name: David Sparks
 Date: 10/30/2025
 Desc: Screen to update an existing student by username.
*/

package com.example.dsparks_homework03_program1;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class UpdateStudentActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private EditText etFirst, etLast, etEmail, etAge, etGpa;
    private Spinner spMajor;
    private String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);

        // username passed from details screen
        username = getIntent().getStringExtra("username");

        // views
        etFirst = findViewById(R.id.etFirstName);
        etLast = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etAge = findViewById(R.id.etAge);
        etGpa = findViewById(R.id.etGpa);
        spMajor = findViewById(R.id.spMajor);
        Button btnSave = findViewById(R.id.btnSave);

        // db and data
        db = new DatabaseHelper(this);
        loadMajors();
        loadStudent();

        // save changes
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void loadMajors() {
        List<Major> majors = db.getAllMajors();
        ArrayAdapter<Major> adp = new ArrayAdapter<Major>(this, android.R.layout.simple_spinner_item, majors);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMajor.setAdapter(adp);
    }

    private void loadStudent() {
        Student s = db.getStudentByUsername(username);
        if (s == null) {
            finish();
            return;
        }
        etFirst.setText(s.getFirstName());
        etLast.setText(s.getLastName());
        etEmail.setText(s.getEmail());
        etAge.setText(String.valueOf(s.getAge()));
        etGpa.setText(String.valueOf(s.getGpa()));

        if (s.getMajorId() != null && spMajor.getAdapter() != null) {
            for (int i = 0; i < spMajor.getAdapter().getCount(); i++) {
                Major m = (Major) spMajor.getAdapter().getItem(i);
                if (m.getId() == s.getMajorId()) {
                    spMajor.setSelection(i);
                    break;
                }
            }
        }
    }

    private void save() {
        String fn = etFirst.getText().toString().trim();
        String ln = etLast.getText().toString().trim();
        String em = etEmail.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String gpaStr = etGpa.getText().toString().trim();

        if (TextUtils.isEmpty(fn) || TextUtils.isEmpty(ln) ||
                TextUtils.isEmpty(em) || TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(gpaStr)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        float gpa;
        try {
            age = Integer.parseInt(ageStr);
            gpa = Float.parseFloat(gpaStr);
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Invalid age or GPA", Toast.LENGTH_SHORT).show();
            return;
        }

        Integer majorId = null;
        Object sel = spMajor.getSelectedItem();
        if (sel instanceof Major) {
            majorId = ((Major) sel).getId();
        }

        Student updated = new Student(fn, ln, username, em, age, gpa, majorId);
        int rows = db.updateStudent(updated);
        if (rows > 0) {
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}
