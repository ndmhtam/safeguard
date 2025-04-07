package com.sg.safeguard;

import java.util.regex.Pattern;

public class ValidateHelper {

  //  Regex for validating Gmail format
  //  private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";

  // Regex kiểm tra định dạng email chung
  // Cho phép các ký tự chữ cái (hoa thường), số, dấu chấm, gạch dưới, phần trăm, cộng và trừ ở phần tên người dùng
  // Theo sau là ký tự @, phần tên miền có thể chứa chữ cái, số, dấu chấm và dấu gạch ngang

  //  email hợp lệ
  //  user@gmail.com
  //  john.doe123@yahoo.co.uk
  //  test_user+label@outlook.com
  //  name.surname@my-domain123.org
  //  abc.def@sub.domain.vn

  //  email không hợp lệ
  //  user@.com (thiếu phần domain trước dấu chấm)
  //  user@gmail (thiếu TLD)
  //  @gmail.com (thiếu phần local)
  //  user@com (TLD quá ngắn)
  private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

  /**
   * Checks if a string is a valid email address.
   *
   * @param email String to check
   * @return true if the string is a valid email address, false otherwise
   */
  public static boolean isValidEmail(String email) {
    if (email == null) return false;
    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    return pattern.matcher(email).matches();
  }

  /**
   * Checks if the confirmation password matches the old password.
   *
   * @param oldPassword Old password
   * @param newPassword Confirmation password
   * @return true if they match, false otherwise
   */
  public static boolean checkConfirmPassword(String oldPassword, String newPassword) {
    return oldPassword.equals(newPassword);
  }
}
