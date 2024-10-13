package com.example.cmaisonneuve;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfilFragment extends Fragment {

    private TextView fullnameTextView;
    private TextView usernameTextView;

    private User user; // Recibiremos el usuario aquí

    public ProfilFragment() {
        // Constructor vacío (requerido)
    }

    // Recibir el usuario desde el Activity o fragmento anterior
    public static ProfilFragment newInstance(User user) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user); // Pasamos el objeto User
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        fullnameTextView = view.findViewById(R.id.fullnameTextView);
        usernameTextView = view.findViewById(R.id.usernameTextView);

        // Setear la información del usuario
        if (user != null) {
            fullnameTextView.setText(user.getFullname());
            usernameTextView.setText(user.getUsername());
        }

        return view;
    }
}
