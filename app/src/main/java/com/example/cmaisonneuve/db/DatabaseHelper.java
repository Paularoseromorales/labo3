package com.example.cmaisonneuve.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.cmaisonneuve.CourseItem;
import com.example.cmaisonneuve.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "school.db";
    private static final int DATABASE_VERSION = 2; // Aumentar la versión para forzar la recreación de la tabla mescurses

    // Tablas
    private static final String TABLE_USERS = "users";
    private static final String TABLE_COURSES = "courses";
    /////////////////////////////////1
    private static final String TABLE_MES_COURSES = "mescurses"; // Nueva tabla para las inscripciones
    /////////////////////////////////1
    // Columnas de usuarios
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_FULL_NAME = "fullname";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_USER_PASSWORD = "password";

    // Columnas de cursos
    private static final String COLUMN_COURSES_ID = "id";
    private static final String COLUMN_COURSES_NAME = "name";
    private static final String COLUMN_COURSES_SIGLE = "sigle";
    private static final String COLUMN_COURSES_TEACHER = "teacher";
    private static final String COLUMN_COURSES_SESSION = "session";
    private static final String COLUMN_COURSES_IMAGE = "image"; // Columna para la imagen
    private static final String COLUMN_COURSES_FILE = "file"; // Columna para el archivo
    //////////////////////////////////////////////////////////////////1
    // Columnas de mescurses (inscripciones)
    private static final String COLUMN_MES_COURSES_ID = "id";
    private static final String COLUMN_MES_COURSES_USER_ID = "user_id";
    private static final String COLUMN_MES_COURSES_COURSE_ID = "course_id";
    private static final String COLUMN_MES_COURSES_NOTE = "note"; // Columna para la nota
    //////////////////////////////////////////////////////////////////1
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Crear tabla de usuarios
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_FULL_NAME + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")";

        // Crear tabla de cursos
        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES + "("
                + COLUMN_COURSES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_COURSES_NAME + " TEXT,"
                + COLUMN_COURSES_SIGLE + " TEXT,"
                + COLUMN_COURSES_TEACHER + " TEXT,"
                + COLUMN_COURSES_SESSION + " TEXT,"
                + COLUMN_COURSES_IMAGE + " BLOB,"
                + COLUMN_COURSES_FILE + " BLOB" + ")";
//////////////////////////////////////////////////////////////////1
        // Crear tabla de inscripciones (mescurses)
        String CREATE_MES_COURSES_TABLE = "CREATE TABLE " + TABLE_MES_COURSES + "("
                + COLUMN_MES_COURSES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MES_COURSES_USER_ID + " INTEGER NOT NULL,"
                + COLUMN_MES_COURSES_COURSE_ID + " INTEGER NOT NULL,"
                + COLUMN_MES_COURSES_NOTE + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_MES_COURSES_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "),"
                + "FOREIGN KEY(" + COLUMN_MES_COURSES_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSES_ID + ")"
                + ")";
//////////////////////////////////////////////////////////////////1
        // Ejecutar las sentencias SQL para crear las tablas
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_COURSES_TABLE);
        //////////////////////////////////////////////////////////////////1
        sqLiteDatabase.execSQL(CREATE_MES_COURSES_TABLE); // Crear la tabla de inscripciones
        //////////////////////////////////////////////////////////////////1
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Eliminar las tablas existentes
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MES_COURSES); // Eliminar la tabla mescurses si existe
        // Recrear las tablas
        onCreate(sqLiteDatabase);
    }

    public boolean insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_FULL_NAME, user.getFullname());
        contentValues.put(COLUMN_USERNAME, user.getUsername());
        contentValues.put(COLUMN_USER_PASSWORD, user.getPassword());
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1; // Retorna true si la inserción fue exitosa
    }
    //////////////////////////////////////////////////////////////////1
    public User checkUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_ID + ", " + COLUMN_USER_FULL_NAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_USER_PASSWORD + "=?", new String[]{user.getUsername(), user.getPassword()});

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FULL_NAME));

            User loggedInUser = new User();
            loggedInUser.setId(userId); // Asigna el ID del usuario
            loggedInUser.setFullname(fullName); // Asigna el nombre completo del usuario

            cursor.close();
            return loggedInUser; // Devuelve el objeto User con el ID y el nombre completo
        }

        if (cursor != null) {
            cursor.close();
        }

        return null; // Devuelve null si las credenciales no son correctas
    }
    //////////////////////////////////////////////////////////////////1

    public User getUserByUsername(String username) {
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
            user.setFullname(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FULL_NAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)));
            cursor.close();
        }

        return user;
    }

    public boolean insertCourses(CourseItem courseItem, byte[] imageData, byte[] fileData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COURSES_NAME, courseItem.getCourseName());
        contentValues.put(COLUMN_COURSES_SIGLE, courseItem.getSigle());
        contentValues.put(COLUMN_COURSES_TEACHER, courseItem.getTeacherName());
        contentValues.put(COLUMN_COURSES_SESSION, courseItem.getSession());

        if (imageData != null) {
            contentValues.put(COLUMN_COURSES_IMAGE, imageData);
        }
        if (fileData != null) {
            contentValues.put(COLUMN_COURSES_FILE, fileData);
        }

        long result = db.insert(TABLE_COURSES, null, contentValues);
        db.close();

        Log.d("DatabaseInsert", "ContentValues: " + contentValues.toString());
        Log.d("DatabaseInsert", "Insert result: " + result);

        return result != -1; // Retorna true si la inserción fue exitosa
    }

    public Cursor getAllCourses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_COURSES, null);
    }
    //////////////////////////////////////////////////////////////////1
    public HashMap<String, Integer> getAllCourseNamesWithIds() {
        HashMap<String, Integer> courseMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_COURSES_ID + ", " + COLUMN_COURSES_NAME + " FROM " + TABLE_COURSES, null);

        if (cursor.moveToFirst()) {
            do {
                int courseId = cursor.getInt(0);
                String courseName = cursor.getString(1);
                courseMap.put(courseName, courseId);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return courseMap;
    }
    //////////////////////////////////////////////////////////////////1
    public List<String> getAllCourseNames() {
        List<String> courseNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_COURSES_NAME + " FROM " + TABLE_COURSES, null);

        if (cursor.moveToFirst()) {
            do {
                String courseName = cursor.getString(0); // Obtener el nombre del curso
                courseNames.add(courseName); // Añadir el nombre a la lista
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return courseNames;
    }


    public boolean updateCourse(CourseItem courseItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COURSES_NAME, courseItem.getCourseName());
        contentValues.put(COLUMN_COURSES_SIGLE, courseItem.getSigle());
        contentValues.put(COLUMN_COURSES_TEACHER, courseItem.getTeacherName());
        contentValues.put(COLUMN_COURSES_SESSION, courseItem.getSession());
        long result = db.update(TABLE_COURSES, contentValues, COLUMN_COURSES_ID + "=?", new String[]{String.valueOf(courseItem.getId())});
        return result > 0; // Retorna true si la actualización fue exitosa
    }

    public boolean deleteCourse(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_COURSES, COLUMN_COURSES_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0; // Retorna true si la eliminación fue exitosa
    }

    public byte[] getCourseImage(int courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_COURSES_IMAGE + " FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSES_ID + "=?", new String[]{String.valueOf(courseId)});

        if (cursor != null && cursor.moveToFirst()) {
            byte[] imageData = cursor.getBlob(0);
            cursor.close();
            return imageData; // Devuelve los datos de la imagen
        }
        return null; // No se encontró la imagen
    }

    public byte[] getCourseFile(int courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_COURSES_FILE + " FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSES_ID + "=?", new String[]{String.valueOf(courseId)});

        if (cursor != null && cursor.moveToFirst()) {
            byte[] fileData = cursor.getBlob(0);
            cursor.close();
            return fileData; // Devuelve los datos del archivo
        }
        return null; // No se encontró el archivo
    }
    //////////////////////////////////////////////////////////////////1
    // Método para insertar en la tabla mescurses (inscripciones)
// Método para insertar en la tabla mescurses (inscripciones)
    public boolean insertUserCourse(int userId, int courseId, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MES_COURSES_USER_ID, userId);  // Asegurarse de que aquí llegue el userId correcto
        contentValues.put(COLUMN_MES_COURSES_COURSE_ID, courseId);
        contentValues.put(COLUMN_MES_COURSES_NOTE, note);

        long result = db.insert(TABLE_MES_COURSES, null, contentValues);
        db.close();

        return result != -1; // Retorna true si la inserción fue exitosa
    }


    // Método para obtener los cursos en los que está inscrito un usuario específico
    public Cursor getCoursesForCurrentUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c." + COLUMN_COURSES_ID + ", c." + COLUMN_COURSES_NAME +
                ", c." + COLUMN_COURSES_SIGLE + ", c." + COLUMN_COURSES_TEACHER +
                ", c." + COLUMN_COURSES_SESSION +
                " FROM " + TABLE_MES_COURSES + " mc " +
                " INNER JOIN " + TABLE_COURSES + " c ON mc." + COLUMN_MES_COURSES_COURSE_ID + " = c." + COLUMN_COURSES_ID +
                " WHERE mc." + COLUMN_MES_COURSES_USER_ID + " = ?";

        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }


    //////////////////////////////////////////////////////////////////1
}
