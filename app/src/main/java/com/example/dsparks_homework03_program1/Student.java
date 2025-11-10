/**
 * Author: David Sparks
 * Date: October 30, 2025
 * Description: Student model data for CRUD operations
 */

package com.example.dsparks_homework03_program1;

public class Student {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private int age;
    private float gpa;
    private Integer majorId;

    public Student(String firstName, String lastName, String username, String email, int age, float gpa, Integer majorId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.age = age;
        this.gpa = gpa;
        this.majorId = majorId;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public int getAge() { return age; }
    public float getGpa() { return gpa; }
    public Integer getMajorId() { return majorId; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setAge(int age) { this.age = age; }
    public void setGpa(float gpa) { this.gpa = gpa; }
    public void setMajorId(Integer majorId) { this.majorId = majorId; }
}
