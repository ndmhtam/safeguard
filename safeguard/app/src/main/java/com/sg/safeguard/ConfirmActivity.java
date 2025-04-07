package com.sg.safeguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmActivity extends AppCompatActivity {
  private EditText otp1, otp2, otp3, otp4, otp5, otp6;
  private String correctCode, username, password, email;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_confirm);

    // Khởi tạo các view
    otp1 = findViewById(R.id.otp1);
    otp2 = findViewById(R.id.otp2);
    otp3 = findViewById(R.id.otp3);
    otp4 = findViewById(R.id.otp4);
    otp5 = findViewById(R.id.otp5);
    otp6 = findViewById(R.id.otp6);
    TextView textViewEmail = findViewById(R.id.text_view_email);
    TextView textViewResend = findViewById(R.id.textViewResend);
    Button buttonConfirm = findViewById(R.id.buttonConfirm);

    // TextWatcher để chuyển focus sang EditText tiếp theo sau khi nhập 1 ký tự
    otp1.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.length() == 1) otp2.requestFocus();
      }
    });

    otp2.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.length() == 1) otp3.requestFocus();
      }
    });

    otp3.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.length() == 1) otp4.requestFocus();
      }
    });

    otp4.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.length() == 1) otp5.requestFocus();
      }
    });

    otp5.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.length() == 1) otp6.requestFocus();
      }
    });

    // Lấy dữ liệu từ Intent
    Intent intent = getIntent();
    correctCode = intent.getStringExtra("verificationCode");
    username = intent.getStringExtra("username");
    password = intent.getStringExtra("password");
    email = intent.getStringExtra("email");

    // Hiển thị email
    String emailSentMessage = getString(R.string.email_sent_message, email);
    textViewEmail.setText(emailSentMessage);

    String resendText = "Didn't receive a code? Resend code";
    SpannableString spannableString = new SpannableString(resendText);
    spannableString.setSpan(new ForegroundColorSpan(0xFFFF5722), resendText.indexOf("Resend code"), resendText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    textViewResend.setText(spannableString);

    // Sự kiện Resend code
    textViewResend.setOnClickListener(v -> {
      EmailSender.sendVerificationEmail(email, correctCode);
      Toast.makeText(ConfirmActivity.this, "Verification code has been resent", Toast.LENGTH_SHORT).show();
    });

    // Sự kiện nút Confirm
    buttonConfirm.setOnClickListener(v -> {
      String enteredCode = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString() + otp5.getText().toString() + otp6.getText().toString();

      if (enteredCode.length() != 6) {
        Toast.makeText(ConfirmActivity.this, "Please enter 6 digits", Toast.LENGTH_SHORT).show();
        return;
      }

      if (enteredCode.equals(correctCode)) {
        DatabaseHelper dbHelper = new DatabaseHelper(ConfirmActivity.this);
        boolean isRegistered = dbHelper.registerUser(username, password, email);
        dbHelper.close();

        if (isRegistered) {
          // Lưu username vào SharedPreferences
          SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
          SharedPreferences.Editor editor = sharedPreferences.edit().putString("username", username);
          editor.apply();

          Toast.makeText(ConfirmActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

          startActivity(new Intent(ConfirmActivity.this, MainActivity.class));
          finish();
        } else {
          Toast.makeText(ConfirmActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
        }
      } else {
        Toast.makeText(ConfirmActivity.this, "Incorrect verification code", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
