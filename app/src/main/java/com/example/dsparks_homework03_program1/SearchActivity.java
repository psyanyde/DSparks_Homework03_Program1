/*
 Name: David Sparks
 Date: 10/30/2025
 Desc: Search and filter screen
*/

package com.example.dsparks_homework03_program1;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private EditText etName, etUsername, etGpaMin, etGpaMax;
    private Spinner spMajor;
    private ListView list;
    private StudentSearchAdapter adapter;
    private List<Student> results = new ArrayList<Student>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // views
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etGpaMin = findViewById(R.id.etGpaMin);
        etGpaMax = findViewById(R.id.etGpaMax);
        spMajor = findViewById(R.id.spMajor);
        list = findViewById(R.id.listSearch);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnClear = findViewById(R.id.btnClear);

        // db + adapter
        db = new DatabaseHelper(this);
        adapter = new StudentSearchAdapter(this, results, db);
        list.setAdapter(adapter);

        loadMajors();

        // search
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });

        // clear
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
    }

    private void loadMajors() {
        List<Major> majors = db.getAllMajors();
        majors.add(0, new Major(0, "Any Major", ""));
        ArrayAdapter<Major> adp = new ArrayAdapter<Major>(this, android.R.layout.simple_spinner_item, majors);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMajor.setAdapter(adp);
    }

    private void doSearch() {
        String name = etName.getText().toString().trim();
        String un = etUsername.getText().toString().trim();
        String gMin = etGpaMin.getText().toString().trim();
        String gMax = etGpaMax.getText().toString().trim();

        Integer majorId = null;
        Object sel = spMajor.getSelectedItem();
        if (sel instanceof Major) {
            Major m = (Major) sel;
            if (m.getId() != 0) {
                majorId = m.getId();
            }
        }

        Float min = null;
        Float max = null;
        if (!TextUtils.isEmpty(gMin)) {
            try { min = Float.parseFloat(gMin); } catch (NumberFormatException ex) { min = null; }
        }
        if (!TextUtils.isEmpty(gMax)) {
            try { max = Float.parseFloat(gMax); } catch (NumberFormatException ex) { max = null; }
        }

        results.clear();
        results.addAll(db.searchStudents(name, un, majorId, min, max));
        adapter.notifyDataSetChanged();
    }

    private void clear() {
        etName.setText("");
        etUsername.setText("");
        etGpaMin.setText("");
        etGpaMax.setText("");
        if (spMajor.getAdapter() != null) {
            spMajor.setSelection(0);
        }
        results.clear();
        adapter.notifyDataSetChanged();
    }
}
