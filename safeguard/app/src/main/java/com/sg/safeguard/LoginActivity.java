package com.sg.safeguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
  private EditText editTextEmail, editTextPassword;
  private DatabaseHelper dbHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    editTextEmail = findViewById(R.id.edit_text_email);
    editTextPassword = findViewById(R.id.edit_text_password);
    Button buttonLogin = findViewById(R.id.button_login);
    dbHelper = new DatabaseHelper(this);

    buttonLogin.setOnClickListener(v -> {
      String email = editTextEmail.getText().toString().trim();
      String password = editTextPassword.getText().toString().trim();

      if (email.isEmpty() || password.isEmpty()) {
        Toast.makeText(LoginActivity.this, "Please fill in all information",
            Toast.LENGTH_SHORT).show();
      } else {
        boolean isValid = dbHelper.checkUserLogin(email, password);
        if (isValid) {
            Toast.makeText(LoginActivity.this, "Login successful",
                    Toast.LENGTH_SHORT).show();

          // Lấy username từ SharedPreferences
          SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
          String username = prefs.getString("username", "User");

          // Chuyển đến activity chính
          Intent intent = new Intent(LoginActivity.this, MainActivity.class);
          intent.putExtra("username", username);
          // Chuyển sang MainActivity
          startActivity(intent);
          finish();
        } else {
            Toast.makeText(LoginActivity.this, "Incorrect username or password",
                    Toast.LENGTH_SHORT).show();
        }
      }
    });

    TextView textViewRegister = findViewById(R.id.register);
    // Sự kiện cho TextView Register
    textViewRegister.setOnClickListener(v -> {
      // Chuyển sang RegisterActivity
      Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
      startActivity(intent);
    });
  }
}
