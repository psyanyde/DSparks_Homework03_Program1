/*
 Name: David Sparks
 Date: 10/30/2025
 Desc: Main screen. Shows students and lets people add/delete/search.
*/

package com.example.dsparks_homework03_program1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private ListView listStudentsView;
    private StudentListAdapter adapter;
    private List<Student> students = new ArrayList<Student>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up views
        listStudentsView = findViewById(R.id.listStudents);
        Button btnAddStudent = findViewById(R.id.btnAddStudent);
        Button btnMajors = findViewById(R.id.btnMajors);
        Button btnSearch = findViewById(R.id.btnSearch);

        // database
        db = new DatabaseHelper(this);

        // adapter for the list
        adapter = new StudentListAdapter(this, students);
        listStudentsView.setAdapter(adapter);

        // click: open details
        listStudentsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student s = students.get(position);
                Intent i = new Intent(MainActivity.this, StudentDetailActivity.class);
                i.putExtra("username", s.getUsername());
                startActivity(i);
            }
        });

        // long click: delete confirm
        listStudentsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Student s = students.get(position);
                confirmDelete(s.getUsername());
                return true;
            }
        });

        // buttons
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddStudentActivity.class));
            }
        });

        btnMajors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddMajorActivity.class));
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
    }

    // simple delete dialog
    private void confirmDelete(final String username) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Student")
                .setMessage("Delete \"" + username + "\" permanently?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int rows = db.deleteStudent(username);
                        if (rows > 0) {
                            Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            refresh();
                        } else {
                            Toast.makeText(MainActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // reload list data
    private void refresh() {
        students.clear();
        students.addAll(db.getAllStudents());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    // menu for search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
