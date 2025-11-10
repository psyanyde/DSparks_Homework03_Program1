/*
 Name: David Sparks
 Date: 10/30/2025
 Desc: Screen to add a new student to the database.
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

public class AddStudentActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private EditText etFirst, etLast, etUser, etEmail, etAge, etGpa;
    private Spinner spMajor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        // views
        etFirst = findViewById(R.id.etFirstName);
        etLast = findViewById(R.id.etLastName);
        etUser = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etAge = findViewById(R.id.etAge);
        etGpa = findViewById(R.id.etGpa);
        spMajor = findViewById(R.id.spMajor);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnClear = findViewById(R.id.btnClear);

        // db
        db = new DatabaseHelper(this);
        loadMajors();

        // save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStudent();
            }
        });

        // clear
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });
    }

    private void loadMajors() {
        List<Major> majors = db.getAllMajors();
        ArrayAdapter<Major> adp = new ArrayAdapter<Major>(this, android.R.layout.simple_spinner_item, majors);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMajor.setAdapter(adp);
    }

    private void saveStudent() {
        // read input
        String fn = etFirst.getText().toString().trim();
        String ln = etLast.getText().toString().trim();
        String un = etUser.getText().toString().trim();
        String em = etEmail.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String gpaStr = etGpa.getText().toString().trim();

        // simple checks
        if (TextUtils.isEmpty(fn) || TextUtils.isEmpty(ln) || TextUtils.isEmpty(un) ||
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

        Student s = new Student(fn, ln, un, em, age, gpa, majorId);
        long row = db.addStudent(s);
        if (row == -1) {
            Toast.makeText(this, "Username must be unique", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show();
            clearFields();
        }
    }

    private void clearFields() {
        etFirst.setText("");
        etLast.setText("");
        etUser.setText("");
        etEmail.setText("");
        etAge.setText("");
        etGpa.setText("");
        if (spMajor.getAdapter() != null && spMajor.getAdapter().getCount() > 0) {
            spMajor.setSelection(0);
        }
    }
}
