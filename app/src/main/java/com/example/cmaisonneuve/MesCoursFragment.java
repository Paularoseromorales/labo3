package com.example.cmaisonneuve;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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

import android.app.AlertDialog;
import android.content.DialogInterface;

public class MesCoursFragment extends Fragment {
    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;
    private List<CourseItem> courseItemList;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_cours, container, false);

        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerview);

        // Inicializar la lista de cursos
        courseItemList = new ArrayList<>();
        db = new DatabaseHelper(getActivity());

        // Configurar el adaptador con el listener de eliminación y visualización
        courseAdapter = new CourseAdapter(courseItemList, course -> {
            // Aquí podrías manejar la modificación si lo deseas
        }, courseId -> {
            // Lógica para mostrar el diálogo de confirmación de eliminación
            showDeleteConfirmationDialog(courseId);
        });

        // Cargar los cursos desde la base de datos
        loadCoursesFromDatabase();

        // Configurar el LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        // Agregar una separación entre los elementos
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Adjuntar el adaptador al RecyclerView
        recyclerView.setAdapter(courseAdapter);

        return view;
    }

    // Método para mostrar el diálogo de confirmación de eliminación
    private void showDeleteConfirmationDialog(int courseId) {
        // Obtener el curso a eliminar para mostrar la información
        CourseItem courseToDelete = null;
        for (CourseItem course : courseItemList) {
            if (course.getId() == courseId) {
                courseToDelete = course;
                break;
            }
        }

        // Verificar si el curso existe
        if (courseToDelete == null) {
            Toast.makeText(getActivity(), "Cours introuvable.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear la alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmation de suppression");
        builder.setMessage("Vous êtes sûr de supprimer le cours : " + courseToDelete.getCourseName() + " ?");

        // Configurar los botones "Oui" y "Non"
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lógica para eliminar el curso
                boolean success = db.deleteCourse(courseId);
                if (success) {
                    Toast.makeText(getActivity(), "Cours supprimé avec succès", Toast.LENGTH_LONG).show();
                    loadCoursesFromDatabase();  // Recargar la lista de cursos
                } else {
                    Toast.makeText(getActivity(), "Erreur lors de la suppression", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cerrar el diálogo sin hacer nada
                dialog.dismiss();
            }
        });

        // Mostrar la alerta
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Método para cargar los cursos desde la base de datos para el usuario actual
    public void loadCoursesFromDatabase() {
        // Obtener el ID del usuario actual desde SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int currentUserId = preferences.getInt("currentUserId", -1);  // Verificar si se recupera el ID correcto

        // Aquí añadimos el Log para comprobar el ID del usuario
        Log.d("MesCoursFragment", "User ID: " + currentUserId);

        if (currentUserId == -1) {
            Toast.makeText(getActivity(), "Aucun utilisateur connecté", Toast.LENGTH_LONG).show();
            return; // Salir si no hay usuario logueado
        }

        // Vaciar la lista antes de recargarla para evitar duplicados
        courseItemList.clear();

        // Obtener los cursos del usuario actual desde la base de datos
        Cursor cursor = db.getCoursesForCurrentUser(currentUserId);  // Asegurarse de que aquí se pasa el ID correcto
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
            // Notificar al adaptador que los datos han cambiado
            courseAdapter.notifyDataSetChanged();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        loadCoursesFromDatabase(); // Recargar los cursos cuando se vuelve a este fragmento
    }
}
