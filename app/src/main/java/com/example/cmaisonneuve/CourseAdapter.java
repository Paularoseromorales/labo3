package com.example.cmaisonneuve;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cmaisonneuve.db.DatabaseHelper;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<CourseItem> courseList;
    private OnItemClickListener listener;
    private OnDeleteClickListener deleteListener;

    // Interfaces pour gérer les clics sur les éléments
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
        holder.titre.setText(courseItem.getCourseName());
        holder.textViewTeacherName.setText(courseItem.getTeacherName());

        SharedPreferences preferences = holder.itemView.getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int currentUserId = preferences.getInt("currentUserId", -1);


        if (currentUserId == 1) {
            holder.btnModifyCourse.setVisibility(View.VISIBLE);
            holder.btnRemoveCourse.setVisibility(View.VISIBLE);
        } else {
            holder.btnModifyCourse.setVisibility(View.GONE);
            holder.btnRemoveCourse.setVisibility(View.GONE);
        }

        if (courseItem.getImage() != null) {

            Bitmap bitmap = BitmapFactory.decodeByteArray(courseItem.getImage(), 0, courseItem.getImage().length);
            holder.courseImageView.setImageBitmap(bitmap);
        } else {

            holder.courseImageView.setImageResource(R.drawable.courseimage);
        }

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

        // Configure le bouton Supprimer
        holder.btnRemoveCourse.setOnClickListener(v -> deleteListener.onDeleteClick(courseItem.getId()));

        holder.titre.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();


            // Crée l'intention de passer à l'activité suivante
            Intent intent = new Intent(context, ViewCoursActivity.class);
            intent.putExtra("course_id", courseItem.getId());
            intent.putExtra("course_name", courseItem.getCourseName());
            intent.putExtra("course_sigle", courseItem.getSigle());
            intent.putExtra("course_teacher", courseItem.getTeacherName());
            intent.putExtra("course_session", courseItem.getSession());
            intent.putExtra("image", courseItem.getImage());
            intent.putExtra("fichier", courseItem.getFile());
            intent.putExtra("current_user_id", currentUserId);
            context.startActivity(intent);
        });

        holder.bind(courseItem, listener);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }


    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView titre;
        TextView textViewSigle;
        TextView textViewTeacherName;
        ImageButton btnRemoveCourse;
        ImageButton btnModifyCourse;
        ImageView courseImageView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            titre = itemView.findViewById(R.id.titre);
            textViewSigle = itemView.findViewById(R.id.sigleCourse);
            textViewTeacherName = itemView.findViewById(R.id.teacherText);
            btnRemoveCourse = itemView.findViewById(R.id.btn_remove_course);
            btnModifyCourse = itemView.findViewById(R.id.btn_modify_course);
            courseImageView = itemView.findViewById(R.id.course_image);
        }

        public void bind(final CourseItem courseItem, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(courseItem));
        }
    }
}