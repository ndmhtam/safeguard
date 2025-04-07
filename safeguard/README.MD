# Cấu Hình Gửi Email và Google Maps API trong Android

## 📧 Tạo App Password cho Gmail (Sử dụng với JavaMail)

> Nếu tài khoản Gmail đã bật xác minh 2 bước (2FA), bạn cần tạo App Password để gửi email qua
> JavaMail. Không thể dùng mật khẩu Gmail thông thường.

### 1. Đăng nhập tài khoản Google

- Truy cập [Google Account](https://myaccount.google.com/)
- Đăng nhập bằng tài khoản Gmail mà bạn muốn sử dụng (ví dụ: `your-email@gmail.com`)

### 2. Bật xác minh 2 bước (2FA)

- Truy cập **Security > 2-Step Verification**
- Làm theo hướng dẫn để bật xác minh 2 bước
- Có thể yêu cầu số điện thoại để xác minh

### 3. Tạo App Password

- Sau khi bật 2FA, quay lại **Security > 2-Step Verification**
- Kéo xuống phần **App passwords**
    - Có thể sẽ cần nhập lại mật khẩu tài khoản
- Nhấn **Generate** để tạo App Password mới
- Chọn ứng dụng: Ví dụ `"Mail"`
- Chọn thiết bị: Ví dụ `"Android App"`
- Nhấn **Generate**
- Google sẽ cung cấp một mật khẩu gồm 16 ký tự, ví dụ:
  ```
  abcd efgh ijkl mnop
  ```
  > **Lưu ý:** Khi sử dụng trong code, **không có dấu cách**

---

## 📜 Lấy API Key từ Google Cloud Console (Maps SDK for Android)

### 1. Tạo API Key

- Truy cập [Google Cloud Console](https://console.cloud.google.com/)
- Tạo một **dự án mới** hoặc chọn dự án hiện có
  - Cách tạo dự án mới trong Google Cloud Console:
  - Truy cập trang chính của Google Cloud Console:👉 https://console.cloud.google.com/ 
  - Ở góc trên bên trái (gần logo Google Cloud), nhấn vào tên dự án hiện tại (nếu có), hoặc “Select a project”. 
  - Trong cửa sổ popup hiện ra, nhấn “New Project” ở góc phải trên cùng. 
  - Nhập thông tin:
    - Project name: Đặt tên tùy ý (VD: AndroidMapsApp) 
    - Organization: Nếu không có, bạn có thể để mặc định hoặc chọn No organization 
    - Location: (nếu có yêu cầu) 
    - Nhấn Create.
    - Chờ vài giây, bạn sẽ thấy thông báo ở góc phải “Your project is ready”.Sau đó, bạn có thể chọn nó trong danh sách dự án.
- Vào **APIs & Services > Identifiers**
- Nhấn **Create Credentials > API Key**
- Nên giới hạn quyền của API Key để tăng tính bảo mật:
    - Giới hạn chỉ sử dụng cho **Maps SDK for Android**

### 2. Bật Maps SDK for Android

- Vào **APIs & Services > Library**
- Tìm **Maps SDK for Android**
- Nhấn **Enable**

### 3. Thêm API Key vào `AndroidManifest.xml`

```xml

<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.example.yourapp">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

    <application android:allowBackup="true" android:label="@string/app_name"
        android:theme="@style/Theme.YourApp">

        <!-- Google Maps API Key -->
        <meta-data android:name="com.google.android.geo.API_KEY" android:value="YOUR_API_KEY" />

        <!-- ... -->
    </application>
</manifest>
```

> **Thay `YOUR_API_KEY` bằng khóa API bạn vừa tạo.**
