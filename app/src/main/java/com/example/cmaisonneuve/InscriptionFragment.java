package com.example.cmaisonneuve;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cmaisonneuve.db.DatabaseHelper;

import java.util.HashMap;
import java.util.List;

public class InscriptionFragment extends Fragment {

    private Spinner spinnerCours;
    private Button inscriptionButton;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inscription, container, false);

        // Referencias a las vistas
        spinnerCours = view.findViewById(R.id.spinner_courses);
        inscriptionButton = view.findViewById(R.id.inscriptionButton);

        // Inicializar la base de datos
        databaseHelper = new DatabaseHelper(getActivity());

        // Método para cargar la lista de cursos y actualizar el spinner
        loadCoursesToSpinner();

        // Manejar el clic en el botón de inscripción
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el curso seleccionado
                String selectedCourseName = spinnerCours.getSelectedItem().toString();
                int selectedCourseId = getCourseIdByName(selectedCourseName); // Obtener el ID del curso

                // Obtener el ID del usuario actual desde SharedPreferences
                SharedPreferences preferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                int currentUserId = preferences.getInt("currentUserId", -1); // Obtener el userId guardado

                if (currentUserId == -1) {
                    Toast.makeText(getActivity(), "Error: Aucun utilisateur connecté.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Agregar la inscripción del usuario a la base de datos
                boolean result = databaseHelper.insertUserCourse(currentUserId, selectedCourseId, "Note: N/A"); // Nota por defecto

                if (result) {
                    Toast.makeText(getActivity(), "Inscription réussie au cours: " + selectedCourseName, Toast.LENGTH_SHORT).show();
                    loadCoursesToSpinner(); // Refrescar el spinner después de la inscripción
                } else {
                    Toast.makeText(getActivity(), "Erreur lors de l'inscription.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar el listener para verificar si hay nuevos cursos cuando se interactúa con el spinner
        spinnerCours.setOnTouchListener((v, event) -> {
            // Refrescar los cursos cada vez que se toca el spinner
            loadCoursesToSpinner();
            return false;
        });

        return view;
    }

    // Método para cargar los cursos en el Spinner
    private void loadCoursesToSpinner() {
        // Obtener la lista de nombres de cursos actualizados desde la base de datos
        List<String> listCours = databaseHelper.getAllCourseNames();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listCours);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCours.setAdapter(adapter);
    }

    // Método para obtener el ID del curso por su nombre
    private int getCourseIdByName(String courseName) {
        HashMap<String, Integer> courseMap = databaseHelper.getAllCourseNamesWithIds();
        return courseMap.get(courseName);
    }
}


