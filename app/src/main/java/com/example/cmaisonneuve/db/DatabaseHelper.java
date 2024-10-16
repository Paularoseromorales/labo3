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

    // Nom et version de la base de données
    private static final String DATABASE_NAME = "school.db";
    private static final int DATABASE_VERSION = 2;

    // tableaux
    public static final String TABLE_USERS = "users";
    private static final String TABLE_COURSES = "courses";
    private static final String TABLE_QUIZZES = "quizzes";
    private static final String TABLE_MES_COURSES = "mescurses";

    // Colonnes utilisateur
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_FULL_NAME = "fullname";
    public static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_USER_PASSWORD = "password";

    // Colonnes de cours
    public static final String COLUMN_COURSES_ID = "id";
    private static final String COLUMN_COURSES_NAME = "name";
    private static final String COLUMN_COURSES_SIGLE = "sigle";
    private static final String COLUMN_COURSES_TEACHER = "teacher";
    private static final String COLUMN_COURSES_SESSION = "session";
    private static final String COLUMN_COURSES_IMAGE = "image";
    private static final String COLUMN_COURSES_FILE = "file";


    // quizzes
    private static final String COLUMN_QUIZ_ID = "id";
    private static final String COLUMN_QUIZ_COURSE_ID = "course_id";

    // q1 1
    private static final String COLUMN_QUIZ_QUESTION_1 = "question1";
    private static final String COLUMN_QUIZ_OPTION1_Q1 = "option1_q1";
    private static final String COLUMN_QUIZ_OPTION2_Q1 = "option2_q1";
    private static final String COLUMN_QUIZ_OPTION3_Q1 = "option3_q1";
    private static final String COLUMN_QUIZ_CORRECT_ANSWER_Q1 = "correct_answer_q1";

    // q 2
    private static final String COLUMN_QUIZ_QUESTION_2 = "question2";
    private static final String COLUMN_QUIZ_OPTION1_Q2 = "option1_q2";
    private static final String COLUMN_QUIZ_OPTION2_Q2 = "option2_q2";
    private static final String COLUMN_QUIZ_OPTION3_Q2 = "option3_q2";
    private static final String COLUMN_QUIZ_CORRECT_ANSWER_Q2 = "correct_answer_q2";

    // q 3
    private static final String COLUMN_QUIZ_QUESTION_3 = "question3";
    private static final String COLUMN_QUIZ_OPTION1_Q3 = "option1_q3";
    private static final String COLUMN_QUIZ_OPTION2_Q3 = "option2_q3";
    private static final String COLUMN_QUIZ_OPTION3_Q3 = "option3_q3";
    private static final String COLUMN_QUIZ_CORRECT_ANSWER_Q3 = "correct_answer_q3";

   //Mes courses
    private static final String COLUMN_MES_COURSES_ID = "id";
    public static final String COLUMN_MES_COURSES_USER_ID = "user_id";
    private static final String COLUMN_MES_COURSES_COURSE_ID = "course_id";
    private static final String COLUMN_MES_COURSES_NOTE = "note";



    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_FULL_NAME + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")";

        String CREATE_USER_COURSES = "CREATE TABLE " + TABLE_COURSES + "("
                + COLUMN_COURSES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_COURSES_NAME + " TEXT,"
                + COLUMN_COURSES_SIGLE + " TEXT,"
                + COLUMN_COURSES_TEACHER + " TEXT,"
                + COLUMN_COURSES_SESSION + " TEXT,"
                + COLUMN_COURSES_IMAGE + " BLOB,"
                + COLUMN_COURSES_FILE + " BLOB" + ")";

        String CREATE_QUIZ_TABLE = "CREATE TABLE " + TABLE_QUIZZES + "("
                + COLUMN_QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_QUIZ_COURSE_ID + " INTEGER,"
                // Pregunta 1
                + COLUMN_QUIZ_QUESTION_1 + " TEXT,"
                + COLUMN_QUIZ_OPTION1_Q1 + " TEXT,"
                + COLUMN_QUIZ_OPTION2_Q1 + " TEXT,"
                + COLUMN_QUIZ_OPTION3_Q1 + " TEXT,"
                + COLUMN_QUIZ_CORRECT_ANSWER_Q1 + " INTEGER,"
                // Pregunta 2
                + COLUMN_QUIZ_QUESTION_2 + " TEXT,"
                + COLUMN_QUIZ_OPTION1_Q2 + " TEXT,"
                + COLUMN_QUIZ_OPTION2_Q2 + " TEXT,"
                + COLUMN_QUIZ_OPTION3_Q2 + " TEXT,"
                + COLUMN_QUIZ_CORRECT_ANSWER_Q2 + " INTEGER,"
                // Pregunta 3
                + COLUMN_QUIZ_QUESTION_3 + " TEXT,"
                + COLUMN_QUIZ_OPTION1_Q3 + " TEXT,"
                + COLUMN_QUIZ_OPTION2_Q3 + " TEXT,"
                + COLUMN_QUIZ_OPTION3_Q3 + " TEXT,"
                + COLUMN_QUIZ_CORRECT_ANSWER_Q3 + " INTEGER" + ")";

        String CREATE_MES_COURSES_TABLE = "CREATE TABLE " + TABLE_MES_COURSES + "("
                + COLUMN_MES_COURSES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MES_COURSES_USER_ID + " INTEGER NOT NULL,"
                + COLUMN_MES_COURSES_COURSE_ID + " INTEGER NOT NULL,"
                + COLUMN_MES_COURSES_NOTE + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_MES_COURSES_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "),"
                + "FOREIGN KEY(" + COLUMN_MES_COURSES_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSES_ID + ")"
                + ")";

        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_COURSES);
        sqLiteDatabase.execSQL(CREATE_QUIZ_TABLE);
        sqLiteDatabase.execSQL(CREATE_MES_COURSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MES_COURSES);
        onCreate(sqLiteDatabase);
    }

    public boolean insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_FULL_NAME, user.getFullname());
        contentValues.put(COLUMN_USERNAME, user.getUsername());
        contentValues.put(COLUMN_USER_PASSWORD, user.getPassword());
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }


    public User checkUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_ID + ", " + COLUMN_USER_FULL_NAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_USER_PASSWORD + "=?", new String[]{user.getUsername(), user.getPassword()});

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FULL_NAME));

            User loggedInUser = new User();
            loggedInUser.setId(userId);
            loggedInUser.setFullname(fullName);

            cursor.close();
            return loggedInUser;
        }

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }

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
            Log.d("DatabaseInsert", "Image size: " + imageData.length);
        } else {
            Log.d("DatabaseInsert", "No image provided");
        }
        if (fileData != null) {
            contentValues.put(COLUMN_COURSES_FILE, fileData);
        }

        long result = db.insert(TABLE_COURSES, null, contentValues);
        db.close();

        // Log de depuración
        Log.d("DatabaseInsert", "ContentValues: " + contentValues.toString());
        Log.d("DatabaseInsert", "Insert result: " + result);

        return result != -1; // Retorna true si la inserción fue exitosa
    }



    public Cursor getAllCourses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_COURSES, null);
    }

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

    public List<String> getAllCourseNames() {
        List<String> courseNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_COURSES_NAME + " FROM " + TABLE_COURSES, null);

        if (cursor.moveToFirst()) {
            do {
                String courseName = cursor.getString(0);
                courseNames.add(courseName);
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
        return result > 0;
    }

    public byte[] getCourseImage(int courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_COURSES_IMAGE + " FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSES_ID + "=?", new String[]{String.valueOf(courseId)});

        if (cursor != null && cursor.moveToFirst()) {
            byte[] imageData = cursor.getBlob(0);
            cursor.close();
            return imageData;
        }
        return null;
    }

    public byte[] getCourseFile(int courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_COURSES_FILE + " FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSES_ID + "=?", new String[]{String.valueOf(courseId)});

        if (cursor != null && cursor.moveToFirst()) {
            byte[] fileData = cursor.getBlob(0);
            cursor.close();
            return fileData;
        }
        return null;
    }


    //methode inserer mes courses
    public boolean insertUserCourse(int userId, int courseId, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MES_COURSES_USER_ID, userId);
        contentValues.put(COLUMN_MES_COURSES_COURSE_ID, courseId);
        contentValues.put(COLUMN_MES_COURSES_NOTE, note);

        long result = db.insert(TABLE_MES_COURSES, null, contentValues);
        db.close();

        return result != -1;
    }


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

    public Cursor getStudentById(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(studentId)});
    }


    public Cursor getUsersForCourse(int courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u." + COLUMN_USER_ID + ", u." + COLUMN_USER_FULL_NAME +
                " FROM " + TABLE_MES_COURSES + " mc " +
                " INNER JOIN " + TABLE_USERS + " u ON mc." + COLUMN_MES_COURSES_USER_ID + " = u." + COLUMN_USER_ID +
                " WHERE mc." + COLUMN_MES_COURSES_COURSE_ID + " = ?";

        return db.rawQuery(query, new String[]{String.valueOf(courseId)});
    }



    public boolean insertQuiz(int courseId,
                              String question1, String option1_q1, String option2_q1, String option3_q1, int correctAnswer_q1,
                              String question2, String option1_q2, String option2_q2, String option3_q2, int correctAnswer_q2,
                              String question3, String option1_q3, String option2_q3, String option3_q3, int correctAnswer_q3) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("course_id", courseId);

        Log.d("DatabaseHelper", "Almacenando quiz con courseId: " + courseId);

        // q 1
        contentValues.put("question1", question1);
        contentValues.put("option1_q1", option1_q1);
        contentValues.put("option2_q1", option2_q1);
        contentValues.put("option3_q1", option3_q1);
        contentValues.put("correct_answer_q1", correctAnswer_q1);

        // q 2
        contentValues.put("question2", question2);
        contentValues.put("option1_q2", option1_q2);
        contentValues.put("option2_q2", option2_q2);
        contentValues.put("option3_q2", option3_q2);
        contentValues.put("correct_answer_q2", correctAnswer_q2);

        // q 3
        contentValues.put("question3", question3);
        contentValues.put("option1_q3", option1_q3);
        contentValues.put("option2_q3", option2_q3);
        contentValues.put("option3_q3", option3_q3);
        contentValues.put("correct_answer_q3", correctAnswer_q3);

        //
        long result = db.insert("quizzes", null, contentValues);

        db.close();
        return result != -1;
    }


    public Cursor getQuizByCourseId(int courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_QUIZZES + " WHERE " + COLUMN_QUIZ_COURSE_ID + "=?", new String[]{String.valueOf(courseId)});
    }

    public boolean areCoursesAlreadyCreated() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_COURSES, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count >= 3;
    }


    public boolean deleteStudentFromCourse(int userId, int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_MES_COURSES, COLUMN_MES_COURSES_USER_ID + "=? AND " + COLUMN_MES_COURSES_COURSE_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(courseId)});
        return result > 0;
    }







}