package com.sg.safeguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
  private EditText editTextUsername, editTextPassword, editTextEmail;
  private String generatedCode;
  private DatabaseHelper dbHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    editTextUsername = findViewById(R.id.edit_text_username);
    editTextPassword = findViewById(R.id.edit_text_password);
    editTextEmail = findViewById(R.id.edit_text_email);
    Button buttonRegister = findViewById(R.id.button_register);
    dbHelper = new DatabaseHelper(this);

    buttonRegister.setOnClickListener(v -> {
      String username = editTextUsername.getText().toString().trim();
      String password = editTextPassword.getText().toString().trim();
      String email = editTextEmail.getText().toString().trim();

      if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
        Toast.makeText(RegisterActivity.this, "Please fill in all information", Toast.LENGTH_SHORT).show();
      } else if (!ValidateHelper.isValidEmail(email)) {
        Toast.makeText(RegisterActivity.this, "Invalid email. Please use Gmail", Toast.LENGTH_SHORT).show();
      } else if (dbHelper.checkEmailExists(email)) {
        Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
      } else if (dbHelper.checkUsernameExists(username)) {
        Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
      } else {
        // Tạo mã xác nhận
        generatedCode = generateVerificationCode();
        // Gửi email
        EmailSender.sendVerificationEmail(email, generatedCode);
        Toast.makeText(RegisterActivity.this, "Mã xác nhận đã được gửi đến email của bạn", Toast.LENGTH_SHORT).show();

        Toast.makeText(RegisterActivity.this, "Verification code has been sent to your email", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, ConfirmActivity.class);
        intent.putExtra("verificationCode", generatedCode);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("email", email);
        startActivity(intent);
      }
    });

    TextView textViewLogin = findViewById(R.id.login);
    // Sự kiện cho TextView Login
    textViewLogin.setOnClickListener(v -> {
      // Chuyển sang LoginActivity
      Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
      startActivity(intent);
    });
  }

  // Phương thức tạo mã xác thực ngẫu nhiên
  private String generateVerificationCode() {
    Random random = new Random();
    int code = 100000 + random.nextInt(900000); // Mã 6 chữ số
    return String.valueOf(code);
  }
}
