package com.example.expensetracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    EditText editUsername, editFirstName, editLastName;
    Button saveUserBtn, changePinBtn;
    MyRepository repository;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editUsername = findViewById(R.id.editUsername);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        saveUserBtn = findViewById(R.id.saveUserBtn);
        changePinBtn = findViewById(R.id.changePinBtn);

        repository = new MyRepository(getApplication());

        // Load the only user from database
        ArrayList<User> users = repository.getAllUsers();
        if (users != null && !users.isEmpty()) {
            currentUser = users.get(0);
            editUsername.setText(currentUser.getUsername());
            editFirstName.setText(currentUser.getFirst_name());
            editLastName.setText(currentUser.getLast_name());
        }

        saveUserBtn.setOnClickListener(view -> {
            String username = editUsername.getText().toString().trim();
            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();

            if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            currentUser.setUsername(username);
            currentUser.setFirst_name(firstName);
            currentUser.setLast_name(lastName);

            repository.updateuser(currentUser);
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
        });

        changePinBtn.setOnClickListener(view -> showChangePinDialog());
    }

    private void showChangePinDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_pin, null);

        EditText oldPin = dialogView.findViewById(R.id.oldPin);
        EditText newPin = dialogView.findViewById(R.id.newPin);
        EditText confirmPin = dialogView.findViewById(R.id.confirmPin);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change PIN");
        builder.setView(dialogView);
        builder.setPositiveButton("Submit", null);
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            Button submitBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            submitBtn.setOnClickListener(v -> {
                String old = oldPin.getText().toString().trim();
                String newp = newPin.getText().toString().trim();
                String conf = confirmPin.getText().toString().trim();

                if (old.isEmpty() || newp.isEmpty() || conf.isEmpty()) {
                    Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!old.equals(String.valueOf(currentUser.getPin()))) {
                    Toast.makeText(this, "Old PIN is incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newp.equals(conf)) {
                    Toast.makeText(this, "New PINs do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                currentUser.setPin(Integer.parseInt(newp));
                repository.updateuser(currentUser);
                Toast.makeText(this, "PIN updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });

        dialog.show();
    }
}
