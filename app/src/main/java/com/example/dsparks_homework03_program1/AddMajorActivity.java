package com.example.dsparks_homework03_program1;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddMajorActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etName, etPrefix;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_major);

        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etMajorName);
        etPrefix = findViewById(R.id.etMajorPrefix);
        Button btnAdd = findViewById(R.id.btnAddMajor);

        btnAdd.setOnClickListener(v -> add());
    }

    private void add() {
        String name = etName.getText().toString().trim();
        String prefix = etPrefix.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(prefix)) {
            Toast.makeText(this, "Fill both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.majorExists(name, prefix)) {
            Toast.makeText(this, "Duplicate major or prefix", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = db.addMajor(name, prefix);
        if (id == -1) {
            Toast.makeText(this, "Duplicate major", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Major added", Toast.LENGTH_SHORT).show();
            etName.setText("");
            etPrefix.setText("");
        }
    }
}
