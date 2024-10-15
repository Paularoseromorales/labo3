package com.example.cmaisonneuve;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmaisonneuve.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;  // Importar para usar AlertDialog
import android.content.DialogInterface;  // Importar para el DialogInterface

public class ListCoursFragment extends Fragment {
    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;
    private List<CourseItem> courseItemList;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_cours, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);

        courseItemList = new ArrayList<>();
        db = new DatabaseHelper(getActivity());

        courseAdapter = new CourseAdapter(courseItemList, course -> {
        }, courseId -> {
            showDeleteConfirmationDialog(courseId);
        });


        loadCoursesFromDatabase();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(courseAdapter);

        return view;
    }

    // Méthode pour afficher la boîte de dialogue de confirmation de suppression
    private void showDeleteConfirmationDialog(int courseId) {
        CourseItem courseToDelete = null;
        for (CourseItem course : courseItemList) {
            if (course.getId() == courseId) {
                courseToDelete = course;
                break;
            }
        }

        if (courseToDelete == null) {
            Toast.makeText(getActivity(), "Cours introuvable.", Toast.LENGTH_SHORT).show();
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmation de suppression");
        builder.setMessage("Vous êtes sûr de supprimer le cours : " + courseToDelete.getCourseName() + " ?");


        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean success = db.deleteCourse(courseId);
                if (success) {
                    Toast.makeText(getActivity(), "Cours supprimé avec succès", Toast.LENGTH_LONG).show();
                    loadCoursesFromDatabase();
                } else {
                    Toast.makeText(getActivity(), "Erreur lors de la suppression", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Méthode pour charger les cours depuis la base de données
    public void loadCoursesFromDatabase() {
        courseItemList.clear();

        Cursor cursor = db.getAllCourses();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "Aucun cours trouvé...", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String sigle = cursor.getString(2);
                String teacher = cursor.getString(3);
                String session = cursor.getString(4);
                courseItemList.add(new CourseItem(id, name, sigle, teacher, session));
            }
            courseAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCoursesFromDatabase();
    }
}
