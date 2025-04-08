package com.sg.safeguard;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
  private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
  private GoogleMap googleMap;
  private FusedLocationProviderClient fusedLocationClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Khởi tạo các view
    TextView textViewGreeting = findViewById(R.id.text_view_greeting);
    ImageView imageViewNotification = findViewById(R.id.image_view_notification);
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

    // Khởi tạo Google Maps
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    assert mapFragment != null;
    mapFragment.getMapAsync(this);

    // Xử lý sự kiện nhấn nút định vị
    FloatingActionButton myLocationButton = findViewById(R.id.my_location_button);
    myLocationButton.setOnClickListener(v -> getCurrentLocation());

    // Xử lý chia sẻ vị trí
    FloatingActionButton shareLocationButton = findViewById(R.id.share_location_button);
    shareLocationButton.setOnClickListener(v -> shareCurrentLocation());

    // Khởi tạo FusedLocationProviderClient để lấy vị trí
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    // Lấy username từ SharedPreferences
    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
    String username = prefs.getString("username", "User");
    String greeting = getString(R.string.greeting_message, username);
    textViewGreeting.setText(greeting);

    // Sự kiện nhấn vào nút zoom in
    FloatingActionButton zoomInButton = findViewById(R.id.zoom_in_button);
    zoomInButton.setOnClickListener(v -> {
      if (googleMap != null) {
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
      }
    });

    // Sự kiện nhấn vào nút zoom out
    FloatingActionButton zoomOutButton = findViewById(R.id.zoom_out_button);
    zoomOutButton.setOnClickListener(v -> {
      if (googleMap != null) {
        googleMap.animateCamera(CameraUpdateFactory.zoomOut());
      }
    });

    // Sự kiện nhấn vào biểu tượng chuông
    imageViewNotification.setOnClickListener(v -> {
      // TODO
      // startActivity(new Intent(MainActivity.this, NotificationActivity.class));
    });

    // Thiết lập Bottom Navigation với setOnItemSelectedListener
    bottomNavigationView.setOnItemSelectedListener(item -> {
      int itemId = item.getItemId();
      if (itemId == R.id.nav_home) {
        return true;
      } else if (itemId == R.id.nav_chat) {
        Toast.makeText(MainActivity.this, "Chat clicked", Toast.LENGTH_SHORT).show();
        return true;
      } else if (itemId == R.id.nav_map) {
        Toast.makeText(MainActivity.this, "Map clicked", Toast.LENGTH_SHORT).show();
        return true;
      } else if (itemId == R.id.nav_call) {
        Intent intent = new Intent(MainActivity.this, SupportActivity.class);
        startActivity(intent);
        return true;
      }
      return false;
    });

    // Đặt mục Home được chọn mặc định
    bottomNavigationView.setSelectedItemId(R.id.nav_home);
  }

  @Override
  public void onMapReady(@NonNull GoogleMap map) {
    googleMap = map;

    // Kiểm tra và yêu cầu quyền vị trí nếu chưa được cấp
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    } else {
      // Nếu quyền đã được cấp, lấy vị trí hiện tại
      getCurrentLocation();
    }
  }

  private void getCurrentLocation() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
      return;
    }

    // Dùng getCurrentLocation để chắc chắn nhận được tọa độ
    CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken()).addOnSuccessListener(this, location -> {
      if (location != null && googleMap != null) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
      } else {
        showDefaultLocation("Location is null");
      }
    }).addOnFailureListener(e -> {
      showDefaultLocation("Failed to get location: " + e.getMessage());
    });
  }

  private void showDefaultLocation(String reason) {
    LatLng daNang = new LatLng(16.0678, 108.2208);
    googleMap.clear();
    googleMap.addMarker(new MarkerOptions().position(daNang).title("Da Nang City Center (Default)"));
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(daNang, 15f));
    Toast.makeText(this, "Unable to get current location. " + reason, Toast.LENGTH_LONG).show();
    Log.e("LocationError", reason);
  }


  private void shareCurrentLocation() {
    // Kiểm tra quyền truy cập vị trí. Nếu chưa có quyền, yêu cầu quyền từ người dùng.
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
      return;
    }

    // Lấy vị trí cuối cùng đã biết bằng FusedLocationProviderClient.
    fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
      // Nếu lấy được vị trí.
      if (location != null) {
        // Lấy vĩ độ và kinh độ từ đối tượng Location.
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        // Tạo URL vị trí trên Google Maps.
        String locationUrl = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (My Location)";

        // Tạo Intent để chia sẻ vị trí.
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "My current location: " + locationUrl);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing My Location");

        startActivity(Intent.createChooser(shareIntent, "Share location via"));
      } else {
        Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
      }
    }).addOnFailureListener(e -> {
      Toast.makeText(this, "Failed to get location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    });
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
        getCurrentLocation();
      } else {
        Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        LatLng daNangCenter = new LatLng(16.0678, 108.2208);
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(daNangCenter).title("Da Nang City Center (Default Location)"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(daNangCenter, 15f));
      }
    }
  }
}
