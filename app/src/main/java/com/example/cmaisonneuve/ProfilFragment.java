package com.example.cmaisonneuve;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfilFragment extends Fragment {

    private TextView fullnameTextView;
    private TextView usernameTextView;
    private TextView logoutTextView;

    private User user;

    public ProfilFragment() {

    }

    // Reçoit l'utilisateur de l'activité ou du fragment précédent
    public static ProfilFragment newInstance(User user) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
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

        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        fullnameTextView = view.findViewById(R.id.fullnameTextView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        logoutTextView = view.findViewById(R.id.action_logout);

        if (user != null) {
            fullnameTextView.setText(user.getFullname());
            usernameTextView.setText(user.getUsername());
        }

        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Borrar las SharedPreferences para cerrar la sesión
                SharedPreferences preferences = getActivity().getSharedPreferences("user_prefs", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();  // Borra todas las preferencias
                editor.apply();

                // Redirigir al LoginActivity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpia la pila de actividades
                startActivity(intent);
                getActivity().finish(); // Finaliza la actividad actual
            }
        });

        return view;
    }
}
