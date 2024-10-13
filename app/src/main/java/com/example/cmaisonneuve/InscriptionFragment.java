package com.example.cmaisonneuve;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inscription, container, false);

        // Referencias a las vistas
        spinnerCours = view.findViewById(R.id.spinner_courses);
        inscriptionButton = view.findViewById(R.id.inscriptionButton);

        // Inicializar la base de datos
        databaseHelper = new DatabaseHelper(getActivity());

        // Obtener la lista de nombres de cursos desde la base de datos
        List<String> listCours = databaseHelper.getAllCourseNames();

        // Crear el adaptador para el spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listCours);
        // Definir el layout para los elementos del spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCours.setAdapter(adapter);

        // Manejar el clic en el botón de inscripción
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedCourse = spinnerCours.getSelectedItem().toString();
                Toast.makeText(getActivity(), "Inscrit au Cours: " + selectedCourse, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}