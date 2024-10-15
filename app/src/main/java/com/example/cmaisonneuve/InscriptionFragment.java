package com.example.cmaisonneuve;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.AdapterView;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cmaisonneuve.db.DatabaseHelper;

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


        spinnerCours = view.findViewById(R.id.spinner_courses);
        inscriptionButton = view.findViewById(R.id.inscriptionButton);

        databaseHelper = new DatabaseHelper(getActivity());

        loadCoursesToSpinner();

        List<String> listCours = databaseHelper.getAllCourseNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listCours);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCours.setAdapter(adapter);

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedCourseName = spinnerCours.getSelectedItem().toString();
                int selectedCourseId = getCourseIdByName(selectedCourseName); // Obtener el ID del curso

                SharedPreferences preferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                int currentUserId = preferences.getInt("currentUserId", -1); // Obtener el userId guardado

                if (currentUserId == -1) {
                    Toast.makeText(getActivity(), "Error: Aucun utilisateur connecté.", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean result = databaseHelper.insertUserCourse(currentUserId, selectedCourseId, "Note: N/A"); // Nota por defecto

                if (result) {
                    Toast.makeText(getActivity(), "Inscription réussie au cours: " + selectedCourseName, Toast.LENGTH_SHORT).show();
                    loadCoursesToSpinner(); // Refrescar el spinner después de la inscripción
                } else {
                    Toast.makeText(getActivity(), "Erreur lors de l'inscription.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerCours.setOnTouchListener((v, event) -> {
            loadCoursesToSpinner();
            return false;
        });

        return view;
    }

    // Méthode pour charger les cours dans le Spinner
    private void loadCoursesToSpinner() {
        List<String> listCours = databaseHelper.getAllCourseNames();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listCours);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCours.setAdapter(adapter);
    }
    // Méthode pour obtenir l'ID du cours par nom
    private int getCourseIdByName(String courseName) {
        HashMap<String, Integer> courseMap = databaseHelper.getAllCourseNamesWithIds();
        return courseMap.get(courseName);
    }
}
