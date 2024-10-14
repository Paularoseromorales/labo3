package com.example.cmaisonneuve;

public class CourseItem {
    private int id; // ID del curso en la base de datos
    private String courseName; // Nombre del curso
    private String sigle; // Sigle del curso
    private String teacherName; // Nombre del docente
    private String session; // Sesión del curso
    private byte[] image; // Datos de la imagen (BLOB)
    private byte[] file; // Datos del archivo (BLOB)

    // Constructor para crear un nuevo curso (sin ID)
    public CourseItem(String courseName, String sigle, String teacherName, String session) {
        this.courseName = courseName;
        this.sigle = sigle;
        this.teacherName = teacherName;
        this.session = session;
    }

    // Constructor para obtener un curso existente (con ID)
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