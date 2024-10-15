package com.example.cmaisonneuve;

public class CourseItem {
    private int id;
    private String courseName;
    private String sigle;
    private String teacherName;
    private String session;
    private byte[] image;
    private byte[] file;


    public CourseItem(String courseName, String sigle, String teacherName, String session) {
        this.courseName = courseName;
        this.sigle = sigle;
        this.teacherName = teacherName;
        this.session = session;
    }


    public CourseItem(int id, String courseName, String sigle, String teacherName, String session) {
        this.id = id;
        this.courseName = courseName;
        this.sigle = sigle;
        this.teacherName = teacherName;
        this.session = session;
    }

    public CourseItem(int id, String courseName, String sigle, String teacherName, String session, byte[] image) {
        this.id = id;
        this.courseName = courseName;
        this.sigle = sigle;
        this.teacherName = teacherName;
        this.session = session;
        this.image = image;
    }
    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}