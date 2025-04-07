package com.sg.safeguard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper implements AutoCloseable {
  private static final String DATABASE_NAME = "safe_guard";
  private static final int DATABASE_VERSION = 1;
  private static final String TABLE_USERS = "users";
  private static final String COLUMN_ID = "id";
  private static final String COLUMN_USERNAME = "username";
  private static final String COLUMN_PASSWORD = "password";
  private static final String COLUMN_EMAIL = "email";

  /**
   * Constructor for DatabaseHelper.
   * Initializes the database with the given context, name, and version.
   *
   * @param context The context in which the database is being created or accessed.
   *                This is typically the application context or activity context.
   */
  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  /**
   * This method is called when the database is created for the first time.
   * It creates the users table with columns for id, username, password, and email.
   *
   * @param db The database being created. This parameter is an instance of SQLiteDatabase.
   *           It allows for executing SQL commands to create tables and perform other database operations.
   */
  @Override
  public void onCreate(SQLiteDatabase db) {
    String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_USERNAME + " TEXT,"
        + COLUMN_PASSWORD + " TEXT,"
        + COLUMN_EMAIL + " TEXT" + ")";
    db.execSQL(CREATE_USERS_TABLE);
  }

  /**
   * This method is called when the database needs to be upgraded.
   * It drops the existing users table and recreates it.
   *
   * @param db         The database being upgraded.
   * @param oldVersion The old database version.
   * @param newVersion The new database version.
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
    onCreate(db);
  }

  /**
   * Registers a new user in the database.
   *
   * @param username The username of the user to register.
   * @param password The password of the user.
   * @param email    The email of the user.
   * @return True if the registration was successful, false otherwise.
   */
  public boolean registerUser(String username, String password, String email) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_USERNAME, username);
    values.put(COLUMN_PASSWORD, password);
    values.put(COLUMN_EMAIL, email);

    long result = db.insert(TABLE_USERS, null, values);
    db.close();
    return result != -1;
  }

  /**
   * Checks if a user with the given email and password exists in the database.
   *
   * @param email The email of the user.
   * @param password The password of the user.
   * @return True if a user with the given credentials exists, false otherwise.
   */
  public boolean checkUserLogin(String email, String password) {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT * FROM " + TABLE_USERS + " WHERE "
        + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
    Cursor cursor = db.rawQuery(query, new String[]{email, password});

    boolean result = cursor.getCount() > 0;
    cursor.close();
    db.close();
    return result;
  }

  /**
   * Checks if a user with the given username exists in the database.
   *
   * @param username The username to check for existence.
   * @return True if a user with the given username exists, false otherwise.
   *         This method queries the database to check if any record matches the given username.
   */
  public boolean checkUsernameExists(String username) {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?";
    Cursor cursor = db.rawQuery(query, new String[]{username});

    boolean exists = cursor.getCount() > 0;
    cursor.close();
    db.close();
    return exists;
  }

  /**
   * Checks if a user with the given email exists in the database.
   *
   * @param email The email to check for existence.
   * @return True if a user with the given email exists, false otherwise.
   *         This method queries the database to check if any record matches the given email.
   */
  public boolean checkEmailExists(String email) {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?";
    Cursor cursor = db.rawQuery(query, new String[]{email});

    boolean exists = cursor.getCount() > 0;
    cursor.close();
    db.close();
    return exists;
  }
}
