package com.example.cmaisonneuve;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<CourseItem> courseList;
    private OnItemClickListener listener;
    private OnDeleteClickListener deleteListener;

    // Interfaces para manejar los clics en los elementos
    public interface OnItemClickListener {
        void onItemClick(CourseItem courseItem);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int courseId);
    }

    // Constructor
    public CourseAdapter(List<CourseItem> courseList, OnItemClickListener listener, OnDeleteClickListener deleteListener) {
        this.courseList = courseList;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseItem courseItem = courseList.get(position);
        holder.titre.setText(courseItem.getCourseName());  // Asignar el nombre del curso al EditText
        holder.textViewSigle.setText(courseItem.getSigle());
        holder.textViewTeacherName.setText(courseItem.getTeacherName());

        // Configurar el botón de modificación
        holder.btnModifyCourse.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, EditCourseActivity.class);
            intent.putExtra("course_id", courseItem.getId());
            intent.putExtra("course_name", courseItem.getCourseName());
            intent.putExtra("course_sigle", courseItem.getSigle());
            intent.putExtra("course_teacher", courseItem.getTeacherName());
            intent.putExtra("course_session", courseItem.getSession());
            context.startActivity(intent);
        });

        // Configurar el botón de eliminación
        holder.btnRemoveCourse.setOnClickListener(v -> deleteListener.onDeleteClick(courseItem.getId()));

        // Configurar el EditText para que funcione como el botón "ver curso"
        holder.titre.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, ViewCoursActivity.class);
            intent.putExtra("course_id", courseItem.getId());  // Pasar la ID del curso
            intent.putExtra("course_name", courseItem.getCourseName());
            intent.putExtra("course_sigle", courseItem.getSigle());
            intent.putExtra("course_teacher", courseItem.getTeacherName());
            intent.putExtra("course_session", courseItem.getSession());
            intent.putExtra("image", courseItem.getImage());  // Pasar imagen si es necesario
            intent.putExtra("fichier", courseItem.getFile());  // Pasar archivo si es necesario
            context.startActivity(intent);
        });

        // Vincula el clic del elemento con el listener general
        holder.bind(courseItem, listener);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    // Clase interna ViewHolder
    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView titre;
        TextView textViewSigle;
        TextView textViewTeacherName;
        ImageButton btnRemoveCourse;
        ImageButton btnModifyCourse;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            titre = itemView.findViewById(R.id.titre);
            textViewSigle = itemView.findViewById(R.id.sigleCourse);
            textViewTeacherName = itemView.findViewById(R.id.teacherText);
            btnRemoveCourse = itemView.findViewById(R.id.btn_remove_course);
            btnModifyCourse = itemView.findViewById(R.id.btn_modify_course);
        }

        public void bind(final CourseItem courseItem, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(courseItem));
        }
    }
}