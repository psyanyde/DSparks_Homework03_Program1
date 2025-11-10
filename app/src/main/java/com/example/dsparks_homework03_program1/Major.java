/**
 * Author: David Sparks
 * Date: October 30, 2025
 * Description: Major model class with major ID, name, and prefix attributes
 */

package com.example.dsparks_homework03_program1;

public class Major {
    private int id;
    private String name;
    private String prefix;

    public Major(int id, String name, String prefix) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPrefix() { return prefix; }

    @Override
    public String toString() { return prefix + " - " + name; }
}
