package com.example.expensetracker;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    private String first_name;

    private String last_name;
    @ColumnInfo(name = "pin")
    private int pin;


    public User(String username, int pin, String first_name, String last_name) {
        this.username = username;
        this.first_name=first_name;
        this.last_name=last_name;
        this.pin = pin;
    }
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getPin() { return pin; }
    public void setPin(int pin) { this.pin = pin; }
}
