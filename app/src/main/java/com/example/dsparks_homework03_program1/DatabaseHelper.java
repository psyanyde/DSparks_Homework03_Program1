/*
 Name: David Sparks
 Date: 11/02/2025
 Desc: SQLite helper for students and majors.
*/

package com.example.dsparks_homework03_program1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cis183_crud.db";
    private static final int DB_VERSION = 1;

    // students
    public static final String T_STUDENTS = "students";
    public static final String COL_FN = "first_name";
    public static final String COL_LN = "last_name";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_AGE = "age";
    public static final String COL_GPA = "gpa";
    public static final String COL_STUDENT_MAJOR_ID = "major_id";

    // majors
    public static final String T_MAJORS = "majors";
    public static final String COL_MAJOR_ID = "id";
    public static final String COL_MAJOR_NAME = "name";
    public static final String COL_MAJOR_PREFIX = "prefix";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create majors first
        String createMajors = "CREATE TABLE " + T_MAJORS + " (" +
                COL_MAJOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MAJOR_NAME + " TEXT NOT NULL, " +
                COL_MAJOR_PREFIX + " TEXT NOT NULL, " +
                "UNIQUE(" + COL_MAJOR_NAME + "), " +
                "UNIQUE(" + COL_MAJOR_PREFIX + ")" +
                ");";

        // then students
        String createStudents = "CREATE TABLE " + T_STUDENTS + " (" +
                COL_USERNAME + " TEXT PRIMARY KEY, " +
                COL_FN + " TEXT NOT NULL, " +
                COL_LN + " TEXT NOT NULL, " +
                COL_EMAIL + " TEXT NOT NULL, " +
                COL_AGE + " INTEGER NOT NULL, " +
                COL_GPA + " REAL NOT NULL, " +
                COL_STUDENT_MAJOR_ID + " INTEGER, " +
                "FOREIGN KEY(" + COL_STUDENT_MAJOR_ID + ") REFERENCES " + T_MAJORS + "(" + COL_MAJOR_ID + ")" +
                ");";

        db.execSQL(createMajors);
        db.execSQL(createStudents);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + T_MAJORS);
        onCreate(db);
    }

    // majors
    public long addMajor(String name, String prefix) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(prefix)) return -1;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_MAJOR_NAME, name.trim());
        cv.put(COL_MAJOR_PREFIX, prefix.trim().toUpperCase());
        return db.insert(T_MAJORS, null, cv);
    }

    public boolean majorExists(String name, String prefix) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_MAJORS,
                new String[]{COL_MAJOR_ID},
                COL_MAJOR_NAME + "=? OR " + COL_MAJOR_PREFIX + "=?",
                new String[]{name.trim(), prefix.trim().toUpperCase()},
                null, null, null);
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public List<Major> getAllMajors() {
        List<Major> list = new ArrayList<Major>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_MAJORS, null, null, null, null, null, COL_MAJOR_NAME + " ASC");
        while (c.moveToNext()) {
            Major m = new Major(
                    c.getInt(c.getColumnIndexOrThrow(COL_MAJOR_ID)),
                    c.getString(c.getColumnIndexOrThrow(COL_MAJOR_NAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_MAJOR_PREFIX))
            );
            list.add(m);
        }
        c.close();
        return list;
    }

    public Major getMajorById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_MAJORS, null, COL_MAJOR_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        Major m = null;
        if (c.moveToFirst()) {
            m = new Major(
                    c.getInt(c.getColumnIndexOrThrow(COL_MAJOR_ID)),
                    c.getString(c.getColumnIndexOrThrow(COL_MAJOR_NAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_MAJOR_PREFIX))
            );
        }
        c.close();
        return m;
    }

    // students
    public long addStudent(Student s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, s.getUsername());
        cv.put(COL_FN, s.getFirstName());
        cv.put(COL_LN, s.getLastName());
        cv.put(COL_EMAIL, s.getEmail());
        cv.put(COL_AGE, s.getAge());
        cv.put(COL_GPA, s.getGpa());
        if (s.getMajorId() != null) {
            cv.put(COL_STUDENT_MAJOR_ID, s.getMajorId());
        }
        return db.insert(T_STUDENTS, null, cv);
    }

    public int deleteStudent(String username) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(T_STUDENTS, COL_USERNAME + "=?", new String[]{username});
    }

    public int updateStudent(Student s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_FN, s.getFirstName());
        cv.put(COL_LN, s.getLastName());
        cv.put(COL_EMAIL, s.getEmail());
        cv.put(COL_AGE, s.getAge());
        cv.put(COL_GPA, s.getGpa());
        if (s.getMajorId() == null) {
            cv.putNull(COL_STUDENT_MAJOR_ID);
        } else {
            cv.put(COL_STUDENT_MAJOR_ID, s.getMajorId());
        }
        return db.update(T_STUDENTS, cv, COL_USERNAME + "=?", new String[]{s.getUsername()});
    }

    public Student getStudentByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_STUDENTS, null, COL_USERNAME + "=?", new String[]{username}, null, null, null);
        Student s = null;
        if (c.moveToFirst()) {
            Integer majorId = c.isNull(c.getColumnIndexOrThrow(COL_STUDENT_MAJOR_ID))
                    ? null : c.getInt(c.getColumnIndexOrThrow(COL_STUDENT_MAJOR_ID));
            s = new Student(
                    c.getString(c.getColumnIndexOrThrow(COL_FN)),
                    c.getString(c.getColumnIndexOrThrow(COL_LN)),
                    c.getString(c.getColumnIndexOrThrow(COL_USERNAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_EMAIL)),
                    c.getInt(c.getColumnIndexOrThrow(COL_AGE)),
                    (float) c.getDouble(c.getColumnIndexOrThrow(COL_GPA)),
                    majorId
            );
        }
        c.close();
        return s;
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<Student>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_STUDENTS, null, null, null, null, null, COL_LN + " ASC, " + COL_FN + " ASC");
        while (c.moveToNext()) {
            Integer majorId = c.isNull(c.getColumnIndexOrThrow(COL_STUDENT_MAJOR_ID))
                    ? null : c.getInt(c.getColumnIndexOrThrow(COL_STUDENT_MAJOR_ID));
            Student s = new Student(
                    c.getString(c.getColumnIndexOrThrow(COL_FN)),
                    c.getString(c.getColumnIndexOrThrow(COL_LN)),
                    c.getString(c.getColumnIndexOrThrow(COL_USERNAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_EMAIL)),
                    c.getInt(c.getColumnIndexOrThrow(COL_AGE)),
                    (float) c.getDouble(c.getColumnIndexOrThrow(COL_GPA)),
                    majorId
            );
            list.add(s);
        }
        c.close();
        return list;
    }

    public List<Student> searchStudents(String nameOrPart, String usernamePart, Integer majorId, Float gpaMin, Float gpaMax) {
        List<String> where = new ArrayList<String>();
        List<String> args = new ArrayList<String>();

        if (!TextUtils.isEmpty(nameOrPart)) {
            where.add("(" + COL_FN + " LIKE ? OR " + COL_LN + " LIKE ?)");
            args.add("%" + nameOrPart + "%");
            args.add("%" + nameOrPart + "%");
        }
        if (!TextUtils.isEmpty(usernamePart)) {
            where.add(COL_USERNAME + " LIKE ?");
            args.add("%" + usernamePart + "%");
        }
        if (majorId != null) {
            where.add(COL_STUDENT_MAJOR_ID + "=?");
            args.add(String.valueOf(majorId));
        }
        if (gpaMin != null) {
            where.add(COL_GPA + ">=?");
            args.add(String.valueOf(gpaMin));
        }
        if (gpaMax != null) {
            where.add(COL_GPA + "<=?");
            args.add(String.valueOf(gpaMax));
        }

        String selection = where.isEmpty() ? null : TextUtils.join(" AND ", where);
        String[] selectionArgs = args.isEmpty() ? null : args.toArray(new String[0]);

        List<Student> out = new ArrayList<Student>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_STUDENTS, null, selection, selectionArgs, null, null, COL_LN + " ASC, " + COL_FN + " ASC");
        while (c.moveToNext()) {
            Integer mj = c.isNull(c.getColumnIndexOrThrow(COL_STUDENT_MAJOR_ID))
                    ? null : c.getInt(c.getColumnIndexOrThrow(COL_STUDENT_MAJOR_ID));
            Student s = new Student(
                    c.getString(c.getColumnIndexOrThrow(COL_FN)),
                    c.getString(c.getColumnIndexOrThrow(COL_LN)),
                    c.getString(c.getColumnIndexOrThrow(COL_USERNAME)),
                    c.getString(c.getColumnIndexOrThrow(COL_EMAIL)),
                    c.getInt(c.getColumnIndexOrThrow(COL_AGE)),
                    (float) c.getDouble(c.getColumnIndexOrThrow(COL_GPA)),
                    mj
            );
            out.add(s);
        }
        c.close();
        return out;
    }
}
