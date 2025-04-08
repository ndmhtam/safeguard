package com.sg.safeguard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SupportActivity extends AppCompatActivity {
    private static final int CALL_PERMISSION_REQUEST_CODE = 2;
    private String tempPhoneNumber; // Lưu số điện thoại tạm thời cho voice call

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        setupCallButton(R.id.police_call_button, "113", false); // Voice call cho Police
        setupCallButton(R.id.hospital_call_button, "115", false); // Voice call cho Hospital
        setupCallButton(R.id.police_camera_button, "113", true); // Video call cho Police
        setupCallButton(R.id.hospital_camera_button, "115", true); // Video call cho Hospital
        setupCallButton(R.id.sos_dn_call_button, "0345828069", false); // Voice call cho SOS DN
        setupCallButton(R.id.sos_dn_camera_button, "0345828069", true); // Video call cho SOS DN
        setupCallButton(R.id.firefighting_call_button, "114", false); // Voice call cho firefighting
        setupCallButton(R.id.firefighting_camera_button, "114", true); // Video call cho firefighting
    }

    private void setupCallButton(int buttonId, final String phoneNumber, final boolean isVideoCall) {
        ImageView button = findViewById(buttonId);
        button.setOnClickListener(v -> initiateCall(phoneNumber, isVideoCall));
    }

    private void initiateCall(String phoneNumber, boolean isVideoCall) {
        if (!isVideoCall) {
            // Voice call yêu cầu quyền CALL_PHONE
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                tempPhoneNumber = phoneNumber;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
            } else {
                makeCall(phoneNumber, false);
            }
        } else {
            makeCall(phoneNumber, true);
        }
    }

    private void makeCall(String phoneNumber, boolean isVideoCall) {
        Intent callIntent = new Intent(isVideoCall ? Intent.ACTION_VIEW : Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (isVideoCall) {
            callIntent.putExtra("video", true);
            if (callIntent.resolveActivity(getPackageManager()) == null) {
                Toast.makeText(this, "No video call app available", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        startActivity(callIntent);
        if (!isVideoCall) {
            tempPhoneNumber = null; // Reset sau khi gọi voice call
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Call permission granted", Toast.LENGTH_SHORT).show();
                if (tempPhoneNumber != null) {
                    makeCall(tempPhoneNumber, false); // Gọi voice call sau khi được cấp quyền
                }
            } else {
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
